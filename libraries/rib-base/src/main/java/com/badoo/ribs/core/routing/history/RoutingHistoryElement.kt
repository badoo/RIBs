package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.INACTIVE
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoutingHistoryElement<C : Parcelable>(
    val routing: Routing<C>,
    internal val activation: Activation = INACTIVE, // FIXME no default for this
    internal val overlays: List<Routing<C>> = emptyList()
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
         * Shrunk to a bundle
         */
        SHRUNK
    }
}
