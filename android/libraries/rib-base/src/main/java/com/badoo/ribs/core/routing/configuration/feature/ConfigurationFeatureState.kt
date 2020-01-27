package com.badoo.ribs.core.routing.configuration.feature

import android.os.Handler
import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.single.Action
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionElement
import io.reactivex.ObservableEmitter
import kotlinx.android.parcel.Parcelize

/**
 * The state of [ConfigurationFeature] in a form that can be persisted to [android.os.Bundle].
 */
@Parcelize
internal data class SavedState<C : Parcelable>(
    val pool: Map<ConfigurationKey, ConfigurationContext.Unresolved<C>>
) : Parcelable {

    /**
     * Converts the [SavedState] to [WorkingState]
     */
    fun toWorkingState(): WorkingState<C> =
        WorkingState(
            SLEEPING,
            pool
        )
}

/**
 * The state [ConfigurationFeature] can work with.
 *
 * @param activationLevel represents the maximum level any [ConfigurationContext] can be activated to. Can be either [SLEEPING] or [ACTIVE].
 * @param pool            represents the pool of all [ConfigurationContext] elements
 */
internal data class WorkingState<C : Parcelable>(
    val activationLevel: ActivationState = SLEEPING,
    val pool: Map<ConfigurationKey, ConfigurationContext<C>> = mapOf(),
    val onGoingTransitions: List<OngoingTransition<C>> = emptyList()
) {
    /**
     * Converts the [WorkingState] to [SavedState] by shrinking all
     * [ConfigurationContext.Resolved] elements back to [ConfigurationContext.Unresolved]
     */
    fun toSavedState(): SavedState<C> =
        SavedState(
            pool.map {
                it.key to when (val entry = it.value) {
                    is ConfigurationContext.Unresolved -> entry
                    is ConfigurationContext.Resolved -> entry.shrink()
                }.copy(
                    activationState = it.value.activationState.sleep()
                )
            }.toMap()
        )
}

internal class OngoingTransition<C : Parcelable>(
    val descriptor: TransitionDescriptor,
    val transition: Transition,
    val actions: List<Action<C>>,
    val transitionElements: List<TransitionElement<C>>,
    val emitter: ObservableEmitter<List<ConfigurationFeature.Effect<C>>>
) {
    private val handler = Handler()

    val runnable = object : Runnable {
        override fun run() {
            actions.forEach { action ->
                if (action.transitionElements.all { it.isFinished() }) {
                    action.onPostTransition()
                    action.transitionElements.forEach { it.markProcessed() }
                }
            }
            if (transitionElements.any { it.isInProgress() }) {
                handler.post(this)
            } else {
                finish()
            }
        }
    }

    fun start() {
        actions.forEach { it.onTransition() }
        emitter.emitEffect(ConfigurationFeature.Effect.TransitionStarted(this))
        runnable.run()
        transition.start()
    }

    fun finish() {
        actions.forEach { it.onFinish() }
        emitter.emitEffect(ConfigurationFeature.Effect.TransitionFinished(this))
        emitter.onComplete()
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
}

data class TransitionDescriptor(
    val from: Any,
    val to: Any
)
