package com.badoo.ribs.core.routing.configuration.feature

import android.os.Handler
import android.os.Parcelable
import android.view.View
import com.badoo.mvicore.element.Actor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.Transaction
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.single.Action
import com.badoo.ribs.core.routing.configuration.action.single.AddAction
import com.badoo.ribs.core.routing.configuration.isBackStackOperation
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import io.reactivex.Observable
import io.reactivex.Observable.fromCallable
import io.reactivex.ObservableEmitter

/**
 * Executes [MultiConfigurationAction] / [SingleConfigurationAction] associated with the incoming
 * [ConfigurationCommand]. The actions will take care of [RoutingAction] invocations and [Node]
 * manipulations, and are expected to return updated elements.
 *
 * Updated elements are then passed on to the [ReducerImpl] in the respective [Effect]s
 */
internal class ConfigurationFeatureActor<C : Parcelable>(
    private val resolver: (C) -> RoutingAction<*>,
    private val parentNode: Node<*>,
    private val transitionHandler: TransitionHandler<C>?
) : Actor<WorkingState<C>, Transaction<C>, ConfigurationFeature.Effect<C>> {

    private val handler = Handler()

    override fun invoke(
        state: WorkingState<C>,
        transaction: Transaction<C>
    ): Observable<ConfigurationFeature.Effect<C>> =
        when (transaction) {
            is MultiConfigurationCommand -> processMultiConfigurationCommand(transaction, state)
            is Transaction.ListOfCommands -> processTransaction(state, transaction)
        }

    private fun processMultiConfigurationCommand(
        transaction: MultiConfigurationCommand<C>,
        state: WorkingState<C>
    ): Observable<ConfigurationFeature.Effect<C>> =
        fromCallable {
            transaction.action.execute(
                pool = state.pool,
                params = createParams(state, transaction)
            )
        }.map { updated ->
            ConfigurationFeature.Effect.Global(
                transaction,
                updated
            )
        }

    private fun processTransaction(
        state: WorkingState<C>,
        transaction: Transaction.ListOfCommands<C>
    ): Observable<ConfigurationFeature.Effect<C>> =
        Observable.create<List<ConfigurationFeature.Effect<C>>> { emitter ->
            state.onGoingTransitions.forEach {
                when {
                    // TODO consider partial! this is only for exit matching, abandon is only for enter matching
                    //  == only exiting part is reverse of new entering
                    transaction.descriptor.isReverseOf(it.descriptor) -> {
                        it.reverse()
                        emitter.onComplete()
                        return@create
                    }
                    transaction.descriptor.isContinuationOf(it.descriptor) -> {
                        it.jumpToEnd()
                    }
                }
            }

            val commands = transaction.commands
            val defaultElements = createDefaultElements(commands)
            val params = createParams(state.copy(pool = state.pool + defaultElements), transaction)
            val actions = createActions(commands, params)
            val effects = createEffects(commands, actions)
            emitter.onNext(effects)

            actions.forEach { it.onBeforeTransition() }
            val transitionElements = actions.flatMap { it.transitionElements }

            if (params.globalActivationLevel == SLEEPING || transitionHandler == null) {
                actions.forEach { it.onTransition() }
                actions.forEach { it.onPostTransition() }
                actions.forEach { it.onFinish() }
                emitter.onComplete()
            } else {
                beginTransitions(
                    transaction.descriptor,
                    transitionElements,
                    emitter,
                    actions
                )
            }
        }.flatMapIterable { it }

    private fun beginTransitions(
        descriptor: TransitionDescriptor,
        transitionElements: List<TransitionElement<C>>,
        emitter: ObservableEmitter<List<ConfigurationFeature.Effect<C>>>,
        actions: List<Action<C>>
    ) {
        requireNotNull(transitionHandler)
        val enteringElements = transitionElements.filter { it.direction == TransitionDirection.Enter }

        enteringElements.visibility(View.INVISIBLE)
        handler.post {
            val transitionPair = transitionHandler.onTransition(transitionElements)
            enteringElements.visibility(View.VISIBLE)

            OngoingTransition(
                descriptor = descriptor,
                direction = TransitionDirection.Exit,
                transitionPair = transitionPair, // TODO split or not?
                actions = actions, // TODO split or not?
                transitionElements = transitionElements, // TODO split or not?
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
     * so that other [ConfigurationCommand]s that rely on their existence can function properly.
     */
    private fun createDefaultElements(
        commands: List<ConfigurationCommand<C>>
    ): Map<ConfigurationKey, ConfigurationContext.Resolved<C>> {

        val defaultElements: MutableMap<ConfigurationKey, ConfigurationContext.Resolved<C>> = mutableMapOf()

        commands.forEach { command ->
            if (command is ConfigurationCommand.Add<C>) {
                defaultElements[command.key] =
                    ConfigurationContext.Unresolved(
                        ConfigurationContext.ActivationState.INACTIVE,
                        command.configuration
                    ).resolveAndAddIfNeeded()
            }
        }

        return defaultElements
    }

    private fun createParams(
        state: WorkingState<C>,
        command: Transaction<C>? = null
    ): ActionExecutionParams<C> =
        ActionExecutionParams(
            resolver = { key ->
                state.resolve(key, null)
            },
            parentNode = parentNode,
            globalActivationLevel = when (command) {
                is MultiConfigurationCommand.Sleep -> SLEEPING
                is MultiConfigurationCommand.WakeUp -> ConfigurationContext.ActivationState.ACTIVE
                else -> state.activationLevel
            }
        )

    private fun createActions(
        commands: List<ConfigurationCommand<C>>,
        params: ActionExecutionParams<C>
    ): List<Action<C>> {
        return commands.map { command ->
            command.actionFactory.create(
                command.key,
                params,
                commands.isBackStackOperation(command.key)
            )
        }
    }

    private fun createEffects(
        commands: List<ConfigurationCommand<C>>,
        actions: List<Action<C>>
    ): List<ConfigurationFeature.Effect<C>> =
        commands.mapIndexed { index, command ->
            ConfigurationFeature.Effect.Individual(
                command,
                actions[index].result
            ) as ConfigurationFeature.Effect<C>
        }

    /**
     * Returns a [Resolved] [ConfigurationContext] looked up by [key].
     *
     * A [ConfigurationContext] should be already present in the pool either in already [Resolved],
     * or [Unresolved] form, the latter of which will be resolved on invocation.
     *
     * The only exception when it's acceptable not to already have an element under [key] is
     * when [defaultElement] is not null, used in the case of the [Add] command.
     */
    private fun WorkingState<C>.resolve(
        key: ConfigurationKey,
        defaultElement: ConfigurationContext.Unresolved<C>?
    ): ConfigurationContext.Resolved<C> {
        val item = pool[key] ?: defaultElement ?: error("Key $key was not found in pool: $pool")

        return item.resolveAndAddIfNeeded()
    }

    private fun ConfigurationContext<C>.resolveAndAddIfNeeded(): ConfigurationContext.Resolved<C> =
        resolve(resolver, parentNode) {
            /**
             * Resolution involves building the associated [Node]s, which need to be guaranteed
             * to be added to the parentNode.
             *
             * Because of this, we need to make sure that [AddAction] is executed every time
             * we resolve, even when no explicit [Add] command was asked.
             *
             * This is to cover cases e.g. when restoring from Bundle:
             * we have a list of [Unresolved] elements that will be resolved on next command
             * (e.g. [WakeUp] / [Activate]), by which time they will need to have been added.
             *
             * [Add] is only called explicitly with direct back stack manipulation, but not on
             * state restoration.
             */
            AddAction.execute(
                it,
                parentNode
            )
        }
}
