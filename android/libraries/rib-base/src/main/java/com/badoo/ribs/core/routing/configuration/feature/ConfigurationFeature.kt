package com.badoo.ribs.core.routing.configuration.feature

import android.os.Handler
import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.Transaction
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.SaveInstanceState
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.configuration.action.single.AddAction
import com.badoo.ribs.core.routing.configuration.action.single.SingleConfigurationAction
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.configuration.action.single.containsInProgress
import com.badoo.ribs.core.routing.configuration.action.single.allTransitionsFinished
import com.badoo.ribs.core.routing.configuration.isBackStackOperation
import com.badoo.ribs.core.routing.transition.ProgressEvaluator
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.fromCallable
import io.reactivex.Observable.fromIterable
import io.reactivex.Single

private val timeCapsuleKey = ConfigurationFeature::class.java.name
private fun <C : Parcelable> TimeCapsule<SavedState<C>>.initialState(): WorkingState<C> =
    (get<SavedState<C>>(timeCapsuleKey)
        ?.let { it.toWorkingState() }
        ?: WorkingState())

/**
 * State store responsible for executing [ConfigurationCommand]s it takes as inputs.
 *
 * The [WorkingState] contains a pool of [ConfigurationContext] elements referenced
 * by [ConfigurationKey] objects. Practically, these keep reference to all configurations
 * currently associated with the RIB: all initial configurations (typically permanent parts
 * and one content type) + the ones coming from back stack changes.
 *
 * Any given [ConfigurationContext] in the pool can be typically in [ACTIVE] or [INACTIVE] state,
 * respective to whether it is active on the screen.
 * Last elements in the back stack are activated, others are deactivated.
 * Permanent parts are added and activated on initialisation and never deactivated as long as
 * the view is available.
 */
internal class ConfigurationFeature<C : Parcelable>(
    initialConfigurations: List<C>,
    timeCapsule: TimeCapsule<SavedState<C>>,
    resolver: (C) -> RoutingAction<*>,
    parentNode: Node<*>,
    transitionHandler: TransitionHandler<C>?
) : ActorReducerFeature<Transaction<C>, Effect<C>, WorkingState<C>, Nothing>(
    initialState = timeCapsule.initialState<C>(),
    bootstrapper = BootStrapperImpl(timeCapsule.initialState<C>(), initialConfigurations),
    actor = ActorImpl(resolver, parentNode, transitionHandler),
    reducer = ReducerImpl()
) {
    init {
        timeCapsule.register(timeCapsuleKey) { state.toSavedState() }
    }

    sealed class Effect<C : Parcelable> {
        data class Global<C : Parcelable>(
            val command: MultiConfigurationCommand<C>,
            val updatedElements: Map<ConfigurationKey, Resolved<C>>
        ) : Effect<C>()

        data class Individual<C : Parcelable>(
            val command: ConfigurationCommand<C>,
            val updatedElement: Resolved<C>
        ) : Effect<C>()
    }

    /**
     * Automatically calls [Add] + [Activate] on all [initialConfigurations]
     */
    class BootStrapperImpl<C : Parcelable>(
        private val initialState: WorkingState<C>,
        private val initialConfigurations: List<C>
    ) : Bootstrapper<Transaction<C>> {

        override fun invoke(): Observable<Transaction<C>> =
            when {
                initialState.pool.isEmpty() -> fromIterable(
                    initialConfigurations
                        .mapIndexed { index, configuration ->
                            val key = ConfigurationKey.Permanent(index)

                            Transaction.ListOfCommands<C>(
                                listOf(
                                    Add(key, configuration),
                                    Activate(key)
                                )
                            )
                        }
                )
                else -> empty()
            }
    }

    /**
     * Executes [MultiConfigurationAction] / [SingleConfigurationAction] associated with the incoming
     * [ConfigurationCommand]. The actions will take care of [RoutingAction] invocations and [Node]
     * manipulations, and are expected to return updated elements.
     *
     * Updated elements are then passed on to the [ReducerImpl] in the respective [Effect]s
     */
    class ActorImpl<C : Parcelable>(
        private val resolver: (C) -> RoutingAction<*>,
        private val parentNode: Node<*>,
        private val transitionHandler: TransitionHandler<C>?
    ) : Actor<WorkingState<C>, Transaction<C>, Effect<C>> {

        private val handler = Handler()

        override fun invoke(
            state: WorkingState<C>,
            transaction: Transaction<C>
        ): Observable<Effect<C>> =
            when (transaction) {
                is MultiConfigurationCommand ->
                    fromCallable {
                        transaction.action.execute(
                            pool = state.pool,
                            params = createParams(transaction, state)
                        )
                    }.map { updated -> Effect.Global(transaction, updated) }

                // FIXME clean up this whole block
                is Transaction.ListOfCommands -> Single.create<List<Effect<C>>> { emitter ->
                    val commands = transaction.commands
                    val defaultElements = mutableMapOf<ConfigurationKey, Resolved<C>>()

                    commands.forEach { command ->
                        if (command is Add<C>) {
                            // TODO maybe move this back to resolve?
                            defaultElements[command.key] =
                                Unresolved(INACTIVE, command.configuration).resolve(
                                    resolver,
                                    parentNode
                                ) {
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
                    }

                    val params = createParams(
                        state.copy(
                            pool = state.pool + defaultElements
                        )
                    )

                    val actions = commands.map { command ->
                        command.actionFactory.create(
                            command.key,
                            params,
                            commands.isBackStackOperation(command.key)
                        )
                    }

                    actions.forEach { it.onBeforeTransition() }
                    val allTransitionElements = actions.flatMap { it.transitionElements }
                    transitionHandler?.onTransition(allTransitionElements)
                    actions.forEach { it.onTransition() }

                    val onFinish = {
                        actions.forEach { it.onFinish() }
                        val effects = commands.mapIndexed { index, command ->
                            Effect.Individual(command, actions[index].result) as Effect<C>
                        }

                        emitter.onSuccess(effects)
                    }

                    val waitForTransitionsToFinish = object : Runnable {
                        override fun run() {
                            actions.forEach { action ->
                                if (action.allTransitionsFinished()) {
                                    action.onPostTransition()
                                    action.transitionElements.forEach {
                                        it.progressEvaluator = ProgressEvaluator.Processed
                                    }
                                }
                            }
                            if (allTransitionElements.containsInProgress()) {
                                handler.post(this)
                            } else {
                                onFinish()
                            }
                        }
                    }

                    if (params.globalActivationLevel == SLEEPING) {
                        actions.forEach { it.onPostTransition() }
                        onFinish()
                    } else {
                        handler.post(waitForTransitionsToFinish)
                    }
                }
                    .toObservable()
                    .flatMapIterable { it }
            }

        // FIXME unify this two if possible
        private fun createParams(
            state: WorkingState<C>
        ): ActionExecutionParams<C> =
            ActionExecutionParams(
                resolver = { key ->
                    state.resolve(key, null)
                },
                parentNode = parentNode,
                globalActivationLevel = state.activationLevel
            )

        private fun createParams(
            command: Transaction<C>,
            state: WorkingState<C>
        ): ActionExecutionParams<C> =
            ActionExecutionParams(
                resolver = { key ->
                    state.resolve(key, null)
                },
                parentNode = parentNode,
                globalActivationLevel = when (command) {
                    is Sleep -> SLEEPING
                    is WakeUp -> ACTIVE
                    else -> state.activationLevel
                }
            )

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
            defaultElement: Unresolved<C>?
        ): Resolved<C> {
            val item = pool[key] ?: defaultElement ?: error("Key $key was not found in pool")

            return item.resolve(resolver, parentNode) {
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
    }

    /**
     * Creates a new [WorkingState] based on the old one, plus the applied [Effect].
     *
     * Involves changing [WorkingState.activationLevel] in case of [Effect.Global],
     * and replacing elements of the [WorkingState.pool] changed by actions in [ActorImpl].
     */
    class ReducerImpl<C : Parcelable> : Reducer<WorkingState<C>, Effect<C>> {
        override fun invoke(state: WorkingState<C>, effect: Effect<C>): WorkingState<C> =
            when (effect) {
                is Effect.Global -> state.global(effect)
                is Effect.Individual -> state.individual(effect)
            }

        private fun WorkingState<C>.global(effect: Effect.Global<C>): WorkingState<C> =
            when (effect.command) {
                is Sleep -> copy(
                    activationLevel = SLEEPING,
                    pool = pool + effect.updatedElements
                )
                is WakeUp -> copy(
                    activationLevel = ACTIVE,
                    pool = pool + effect.updatedElements
                )
                is SaveInstanceState -> copy(
                    pool = pool + effect.updatedElements
                )
            }

        private fun WorkingState<C>.individual(effect: Effect.Individual<C>): WorkingState<C> {
            val key = effect.command.key
            val updated = effect.updatedElement

            return when (effect.command) {
                is Add -> copy(
                    pool = pool.plus(key to updated)
                )
                is Activate,
                is Deactivate -> copy(
                    pool = pool.minus(key).plus(key to updated)
                )
                is Remove -> copy(
                    pool = pool.minus(key)
                )
            }
        }
    }
}
