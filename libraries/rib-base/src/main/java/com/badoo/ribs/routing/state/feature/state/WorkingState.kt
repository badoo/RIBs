package com.badoo.ribs.routing.state.feature.state

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.state.Pool
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.feature.OngoingTransition
import com.badoo.ribs.routing.state.feature.PendingTransition
import com.badoo.ribs.routing.state.poolOf

/**
 * The state [RoutingStatePool] can work with.
 *
 * @param activationLevel represents the maximum level any [RoutingContext] can be activated to. Can be either [SLEEPING] or [ACTIVE].
 * @param pool            represents the pool of all [RoutingContext] elements
 */
internal data class WorkingState<C : Parcelable>(
    val activationLevel: RoutingContext.ActivationState = RoutingContext.ActivationState.SLEEPING,
    val pool: Pool<C> = poolOf(),
    val pendingDeactivate: Set<Routing<C>> = setOf(),
    val pendingRemoval: Set<Routing<C>> = setOf(),
    val ongoingTransitions: List<OngoingTransition<C>> = emptyList(),
    val pendingTransitions: List<PendingTransition<C>> = emptyList()
) {
    /**
     * Converts the [WorkingState] to [SavedState] by shrinking all
     * [RoutingContext.Resolved] elements back to [RoutingContext.Unresolved]
     */
    fun toSavedState(): SavedState<C> =
        SavedState(
            pool.filter { !pendingRemoval.contains(it.key) }
                .map { entry ->
                    val original = entry.value
                    val pendingDeactivateApplied = when {
                        !pendingDeactivate.contains(entry.key) -> original
                        else -> original.withActivationState(
                            activationState = RoutingContext.ActivationState.INACTIVE
                        )
                    }

                    entry.key to pendingDeactivateApplied.shrink()
                }
                .toMap()
        )
}

internal fun <C : Parcelable> WorkingState<C>.withDefaults(defaults: Pool<C>) =
    copy(
        // Defaults should not overwrite existing elements
        pool = pool + defaults + pool
    )
