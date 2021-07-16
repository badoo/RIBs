package com.badoo.ribs.routing.history

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.INACTIVE
import kotlinx.parcelize.Parcelize

/**
 * Marks a [Routing] with [Activation] status, and lists
 * associated overlays.
 *
 * Overlays are other [Routing]s having the same activation as the main element, i.e. they are
 * shown / hidden together.
 *
 * Overlay indices are meant to be stable - this is the responsibility of [RoutingSource]
 * implementations.
 */
@Parcelize
data class RoutingHistoryElement<C : Parcelable>(
    val routing: Routing<C>,
    internal val activation: Activation = INACTIVE,
    val overlays: List<Routing<C>> = emptyList()
) : Parcelable {

    enum class Activation {
        /**
         * On-screen
         */
        ACTIVE,

        /**
         * Off-screen, but alive
         */
        INACTIVE,

        /**
         * Shrunk to a bundle. Unsupported at the moment.
         */
        SHRUNK
    }
}
