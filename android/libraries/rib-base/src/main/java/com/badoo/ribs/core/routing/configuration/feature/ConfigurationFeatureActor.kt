package com.badoo.ribs.core.routing.configuration.feature

import android.os.Handler
import android.os.Parcelable
import android.view.View
import com.badoo.mvicore.element.Actor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.Transaction
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.single.Action
import com.badoo.ribs.core.routing.configuration.action.single.AddAction
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect.TransitionFinished
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect.TransitionStarted
import com.badoo.ribs.core.routing.configuration.isBackStackOperation
import com.badoo.ribs.core.routing.transition.Transition
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
    private var waitForTransitionsToFinish: Runnable? = null

    override fun invoke(
        state: WorkingState<C>,
        transaction: Transaction<C>
    ): Observable<ConfigurationFeature.Effect<C>> =
        when (transaction) {
            is MultiConfigurationCommand ->
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

            is Transaction.ListOfCommands -> processTransaction(state, transaction)
        }

    private fun processTransaction(
        state: WorkingState<C>,
        transaction: Transaction.ListOfCommands<C>
    ): Observable<ConfigurationFeature.Effect<C>> =
        Observable.create<List<ConfigurationFeature.Effect<C>>> { emitter ->
            if (state.onGoingTransitions.isNotEmpty()) {
                state.onGoingTransitions.forEach { it.end() }
                waitForTransitionsToFinish?.let {
                    handler.removeCallbacks(it)
                    it.run()
                }
            }

            val commands = transaction.commands
            val defaultElements = createDefaultElements(commands)
            val params = createParams(state.copy(pool = state.pool + defaultElements), transaction)
            val actions = createActions(commands, params)
            val effects = createEffects(commands, actions)

            actions.forEach { it.onBeforeTransition() }
            val allTransitionElements = actions.flatMap { it.transitionElements }
            val enteringElements = allTransitionElements.filter { it.direction == TransitionDirection.Enter }

            if (params.globalActivationLevel == ConfigurationContext.ActivationState.SLEEPING) {
                actions.forEach { it.onTransition() }
                actions.forEach { it.onPostTransition() }
                actions.forEach { it.onFinish() }
                emitter.onNext(effects)
                emitter.onComplete()
            } else {
                beginTransitions(
                    enteringElements,
                    allTransitionElements,
                    emitter,
                    actions,
                    effects
                )
            }
        }
            .flatMapIterable { it }

    private fun beginTransitions(
        enteringElements: List<TransitionElement<C>>,
        allTransitionElements: List<TransitionElement<C>>,
        emitter: ObservableEmitter<List<ConfigurationFeature.Effect<C>>>,
        actions: List<Action<C>>,
        effects: List<ConfigurationFeature.Effect<C>>
    ) {
        var transition: Transition? = null

        if (transitionHandler != null) {
            enteringElements.visibility(View.INVISIBLE)
            handler.post {
                transition = transitionHandler.onTransition(allTransitionElements)
                transition?.start()
                transition?.signalTransitionStart(emitter)
                enteringElements.visibility(View.VISIBLE)
            }
        }

        actions.forEach { it.onTransition() }

        waitForTransitionsToFinish = object : Runnable {
            override fun run() {
                actions.forEach { action ->
                    if (action.transitionElements.all { it.isFinished() }) {
                        action.onPostTransition()
                        action.transitionElements.forEach { it.markProcessed() }
                    }
                }
                if (allTransitionElements.any { it.isInProgress() }) {
                    handler.post(this)
                } else {
                    actions.forEach { it.onFinish() }
                    transition?.signalTransitionFinished(emitter)
                    emitter.onComplete()
                }
            }
        }

        handler.post(waitForTransitionsToFinish)
        emitter.onNext(effects)
    }

    private fun List<TransitionElement<C>>.visibility(visibility: Int) {
        forEach {
            it.view.visibility = visibility
        }
    }

    private fun Transition.signalTransitionStart(
        emitter: ObservableEmitter<List<ConfigurationFeature.Effect<C>>>
    ) {
        emitter.emitEffect(TransitionStarted(this))
    }

    private fun Transition.signalTransitionFinished(
        emitter: ObservableEmitter<List<ConfigurationFeature.Effect<C>>>
    ) {
        emitter.emitEffect(TransitionFinished(this))
    }

    private fun ObservableEmitter<List<ConfigurationFeature.Effect<C>>>.emitEffect(
        effect: ConfigurationFeature.Effect<C>
    ) {
        onNext(
            listOf(
                effect
            )
        )
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
                is MultiConfigurationCommand.Sleep -> ConfigurationContext.ActivationState.SLEEPING
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
