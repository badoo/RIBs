package com.badoo.ribs.routing.state.feature.state

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.state.RoutingContext
import kotlinx.parcelize.Parcelize

/**
 * The state of [RoutingStatePool] in a form that can be persisted to [android.os.Bundle].
 */
@Parcelize
internal data class SavedState<C : Parcelable>(
    val pool: Map<Routing<C>, RoutingContext.Unresolved<C>>
) : Parcelable {

    /**
     * Converts the [SavedState] to [WorkingState]
     */
    fun toWorkingState(): WorkingState<C> =
        WorkingState(
            RoutingContext.ActivationState.SLEEPING,
            pool
        )
}
