package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.RoutingContext
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.RoutingContext.Unresolved
import com.badoo.ribs.core.routing.history.Routing
import kotlinx.android.parcel.Parcelize

/**
 * The state of [ConfigurationFeature] in a form that can be persisted to [android.os.Bundle].
 */
@Parcelize
internal data class SavedState<C : Parcelable>(
    val pool: Map<Routing<C>, Unresolved<C>>
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
 * @param activationLevel represents the maximum level any [RoutingContext] can be activated to. Can be either [SLEEPING] or [ACTIVE].
 * @param pool            represents the pool of all [RoutingContext] elements
 */
internal data class WorkingState<C : Parcelable>(
    val activationLevel: ActivationState = SLEEPING,
    val pool: Pool<C> = poolOf(),
    val pendingDeactivate: Set<Routing<C>> = setOf(),
    val pendingRemoval: Set<Routing<C>> = setOf(),
    val ongoingTransitions: List<OngoingTransition<C>> = emptyList()
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
                            activationState = INACTIVE
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
