package com.badoo.ribs.routing.state.feature

import android.os.Handler
import android.os.Parcelable
import android.view.View
import com.badoo.mvicore.element.Actor as MviCoreActor
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.activator.RoutingActivator
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.routing.state.MutablePool
import com.badoo.ribs.routing.state.Pool
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.routing.state.action.ActionExecutionParams
import com.badoo.ribs.routing.state.action.TransactionExecutionParams
import com.badoo.ribs.routing.state.action.single.ReversibleAction
import com.badoo.ribs.routing.state.exception.CommandExecutionException
import com.badoo.ribs.routing.state.exception.KeyNotFoundInPoolException
import com.badoo.ribs.routing.state.feature.Transaction.PoolCommand
import com.badoo.ribs.routing.state.feature.Transaction.RoutingChange
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.badoo.ribs.routing.state.feature.state.withDefaults
import com.badoo.ribs.routing.state.mutablePoolOf
import com.badoo.ribs.routing.state.toMutablePool
import com.badoo.ribs.routing.state.changeset.RoutingCommand
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.state.changeset.addedOrRemoved
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import io.reactivex.Observable

/**
 * Executes side-effects of state update.
 */
@SuppressWarnings("LargeClass") // TODO extract
internal class Actor<C : Parcelable>(
    private val resolver: RoutingResolver<C>,
    private val activator: RoutingActivator<C>,
    private val parentNode: Node<*>,
    private val transitionHandler: TransitionHandler<C>?
) : MviCoreActor<WorkingState<C>, Transaction<C>, RoutingStatePool.Effect<C>> {

    private val handler = Handler()

    override fun invoke(
        state: WorkingState<C>,
        transaction: Transaction<C>
    ): Observable<RoutingStatePool.Effect<C>> =
        when (transaction) {
            is PoolCommand -> processPoolCommand(state, transaction)
            is RoutingChange -> processRoutingChange(state, transaction)
        }

    private fun processPoolCommand(
        state: WorkingState<C>,
        transaction: PoolCommand<C>
    ): Observable<RoutingStatePool.Effect<C>> =
        Observable.create { emitter ->
            transaction.action.execute(state, createParams(emitter, state, emptyMap(), transaction))
        }

    private enum class NewTransitionsExecution {
        ABORT, CONTINUE
    }

    private fun processRoutingChange(
        state: WorkingState<C>,
        transaction: RoutingChange<C>
    ): Observable<RoutingStatePool.Effect<C>> =
        Observable.create { emitter ->
            val commands = transaction.changeset
            val defaultElements = createDefaultElements(state, commands)
            val params = createParams(
                emitter = emitter,
                state = state.withDefaults(defaultElements),
                defaultElements = defaultElements,
                transaction = transaction
            )

            if (checkOngoingTransitions(state, transaction) == NewTransitionsExecution.ABORT) {
                emitter.onComplete()
                return@create
            }

            val actions = createActions(commands, params)
            actions.forEach { it.onBeforeTransition() }
            val transitionElements = actions.flatMap { it.transitionElements }

            if (params.globalActivationLevel == SLEEPING || transitionHandler == null) {
                actions.forEach { it.onTransition() }
                actions.forEach { it.onFinish() }
                emitter.onComplete()
            } else {
                beginTransitions(transaction.descriptor, transitionElements, emitter, actions)
            }
        }

    private fun checkOngoingTransitions(
        state: WorkingState<C>,
        transaction: RoutingChange<C>
    ): NewTransitionsExecution {
        state.ongoingTransitions.forEach {
            when {
                transaction.descriptor.isReverseOf(it.descriptor) -> {
                    it.reverse()
                    return NewTransitionsExecution.ABORT
                }
                transaction.descriptor.isContinuationOf(it.descriptor) -> {
                    it.jumpToEnd()
                }
            }
        }

        return NewTransitionsExecution.CONTINUE
    }

    private fun beginTransitions(
        descriptor: TransitionDescriptor,
        transitionElements: List<TransitionElement<C>>,
        emitter: EffectEmitter<C>,
        actions: List<ReversibleAction<C>>
    ) {
        requireNotNull(transitionHandler)
        val enteringElements = transitionElements.filter { it.direction == TransitionDirection.ENTER }

        /**
         * Entering views at this point are created but will be measured / laid out the next frame.
         * We need to base calculations in transition implementations based on their actual measurements,
         * but without them appearing just yet to avoid flickering.
         * Making them invisible, starting the transitions then making them visible achieves the above.
         */
        enteringElements.visibility(View.INVISIBLE)
        handler.post {
            val transitionPair = transitionHandler.onTransition(transitionElements)
            enteringElements.visibility(View.VISIBLE)

            // TODO consider whether splitting this two two instances (one per direction, so that
            //  enter and exit can be controlled separately) is better
            OngoingTransition(
                descriptor = descriptor,
                direction = TransitionDirection.EXIT,
                transitionPair = transitionPair,
                actions = actions,
                transitionElements = transitionElements,
                emitter = emitter
            ).start()
        }
    }

    private fun List<TransitionElement<C>>.visibility(visibility: Int) {
        forEach {
            it.view.visibility = visibility
        }
    }

    /**
     * Since the state doesn't yet reflect elements we're just about to add, we'll create them ahead
     * so that other [RoutingCommand]s that rely on their existence can function properly.
     */
    private fun createDefaultElements(
        state: WorkingState<C>,
        commands: List<RoutingCommand<C>>
    ): Pool<C> {
        val defaultElements: MutablePool<C> = mutablePoolOf()

        commands.forEach { command ->
            if (command is RoutingCommand.Add<C> && !state.pool.containsKey(command.routing) && !defaultElements.containsKey(command.routing)) {
                defaultElements[command.routing] = RoutingContext.Unresolved(
                    activationState = RoutingContext.ActivationState.INACTIVE,
                    routing = command.routing
                )
            }
        }

        return defaultElements
    }

    private fun createParams(
        emitter: EffectEmitter<C>,
        state: WorkingState<C>,
        defaultElements: Pool<C>,
        transaction: Transaction<C>? = null
    ): TransactionExecutionParams<C> {
        val tempPool: MutablePool<C> = state.pool.toMutablePool()

        return TransactionExecutionParams(
            emitter = emitter,
            resolver = { key ->
                val lookup = tempPool[key]
                if (lookup is RoutingContext.Resolved) lookup
                else {
                    val item = defaultElements[key] ?: state.pool[key] ?: throw KeyNotFoundInPoolException(key, state.pool)
                    val resolved = item.resolve(resolver, parentNode)
                    tempPool[key] = resolved
                    resolved
                }
            },
            activator = activator,
            parentNode = parentNode,
            globalActivationLevel = when (transaction) {
                is PoolCommand.Sleep -> SLEEPING
                is PoolCommand.WakeUp -> RoutingContext.ActivationState.ACTIVE
                else -> state.activationLevel
            }
        )
    }

    private fun createActions(
        commands: List<RoutingCommand<C>>,
        params: TransactionExecutionParams<C>
    ): List<ReversibleAction<C>> = commands.map { command ->
        try {
            command.actionFactory.create(
                ActionExecutionParams(
                    transactionExecutionParams = params,
                    command = command,
                    routing = command.routing,
                    addedOrRemoved = commands.addedOrRemoved(command.routing)
                )
            )
        } catch (e: KeyNotFoundInPoolException) {
            throw CommandExecutionException(
                "Could not execute command: ${command::class.java}", e
            )
        }
    }
}

