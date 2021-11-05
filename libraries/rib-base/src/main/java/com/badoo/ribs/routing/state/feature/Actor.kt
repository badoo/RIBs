package com.badoo.ribs.routing.state.feature

import android.os.Parcelable
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
import com.badoo.ribs.routing.state.changeset.RoutingCommand
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.state.changeset.addedOrRemoved
import com.badoo.ribs.routing.state.exception.CommandExecutionException
import com.badoo.ribs.routing.state.exception.KeyNotFoundInPoolException
import com.badoo.ribs.routing.state.feature.Transaction.InternalTransaction
import com.badoo.ribs.routing.state.feature.Transaction.InternalTransaction.ExecutePendingTransition
import com.badoo.ribs.routing.state.feature.Transaction.PoolCommand
import com.badoo.ribs.routing.state.feature.Transaction.RoutingChange
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.badoo.ribs.routing.state.feature.state.withDefaults
import com.badoo.ribs.routing.state.mutablePoolOf
import com.badoo.ribs.routing.state.toMutablePool
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.handler.TransitionHandler

/**
 * Executes side-effects of state update.
 */
@SuppressWarnings("LargeClass", "LongParameterList") // TODO extract
internal class Actor<C : Parcelable>(
    private val resolver: RoutingResolver<C>,
    private val activator: RoutingActivator<C>,
    private val parentNode: Node<*>,
    private val transitionHandler: TransitionHandler<C>?,
    private val effectEmitter: EffectEmitter<C>,
    private val pendingTransitionFactory: PendingTransitionFactory<C>
) {

    fun invoke(state: WorkingState<C>, transaction: Transaction<C>) {
        when (transaction) {
            is PoolCommand -> processPoolCommand(state, transaction)
            is RoutingChange -> processRoutingChange(state, transaction)
            is InternalTransaction -> processInternalTransaction(state, transaction)
        }
    }

    private fun processPoolCommand(
        state: WorkingState<C>,
        transaction: PoolCommand<C>
    ) {
        transaction.action.execute(state, createParams(effectEmitter, state, emptyMap(), transaction))
    }

    private enum class NewTransitionsExecution {
        ABORT, CONTINUE
    }

    private fun processRoutingChange(
        state: WorkingState<C>,
        transaction: RoutingChange<C>
    ) {
        val commands = transaction.changeset
        val defaultElements = createDefaultElements(state, commands)
        val params = createParams(
            emitter = effectEmitter,
            state = state.withDefaults(defaultElements),
            defaultElements = defaultElements,
            transaction = transaction
        )

        checkPendingTransitions(state, transaction)

        if (checkOngoingTransitions(state, transaction) == NewTransitionsExecution.ABORT) {
            return
        }

        val actions = createActions(commands, params)
        actions.forEach { it.onBeforeTransition() }
        val transitionElements = actions.flatMap { it.transitionElements }

        if (canTransition(params) && transitionElements.isNotEmpty()) {
            scheduleTransitions(transaction.descriptor, transitionElements, actions)
        } else {
            actions.forEach { it.onTransition() }
            actions.forEach { it.onFinish() }
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

    private fun checkPendingTransitions(
        state: WorkingState<C>,
        transaction: RoutingChange<C>
    ): NewTransitionsExecution {
        state.pendingTransitions.forEach { pendingTransition ->
            when {
                transaction.descriptor.isReverseOf(pendingTransition.descriptor) -> {
                    pendingTransition.discard()
                }
                transaction.descriptor.isContinuationOf(pendingTransition.descriptor) -> {
                    pendingTransition.completeWithoutTransition()
                }
            }
        }
        return NewTransitionsExecution.CONTINUE
    }

    private fun scheduleTransitions(
        descriptor: TransitionDescriptor,
        transitionElements: List<TransitionElement<C>>,
        actions: List<ReversibleAction<C>>
    ) {
        requireNotNull(transitionHandler)

        pendingTransitionFactory.create(
            descriptor = descriptor,
            direction = TransitionDirection.EXIT,
            actions = actions,
            transitionElements = transitionElements,
        ).schedule()
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

    private fun processInternalTransaction(state: WorkingState<C>, internalTransaction: InternalTransaction<C>) {
        val params = createParams(
            emitter = effectEmitter,
            state = state,
            defaultElements = emptyMap(),
            transaction = internalTransaction
        )

        when (internalTransaction) {
            is ExecutePendingTransition -> consumeTransition(state, internalTransaction.pendingTransition, params)
        }
    }

    private fun consumeTransition(state: WorkingState<C>, pendingTransition: PendingTransition<C>, params: TransactionExecutionParams<C>) {
        val isScheduled = pendingTransition.isScheduled(state)
        val canTransition = canTransition(params)
        when {
            isScheduled && canTransition -> pendingTransition.execute(requireNotNull(transitionHandler)).start()
            isScheduled && !canTransition -> pendingTransition.completeWithoutTransition()
            else -> pendingTransition.discard()
        }
    }

    private fun <C : Parcelable> PendingTransition<C>.isScheduled(state: WorkingState<C>) =
        this in state.pendingTransitions

    private fun canTransition(params: TransactionExecutionParams<C>) =
        params.globalActivationLevel != SLEEPING && transitionHandler != null
}
