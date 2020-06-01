package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.INACTIVE
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoutingHistoryElement<C : Parcelable>(
    val routing: Routing<C>,
    // differ will not make an assumption that last one is always active,
    // instead allows it to be controlled via this flag
    internal val activation: Activation = INACTIVE, // FIXME no default for this
    internal val overlays: List<Routing<C>> = emptyList()
) : Parcelable {

    // FIXME check if can be unified
    //  with [com.badoo.ribs.core.routing.configuration.ActivationState]
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
