package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.INACTIVE
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class RoutingHistoryElement<C : Parcelable>(
    val routing: Routing<C>,
    // FIXME no default for this:
    // differ will not make an assumption that last one is always active, instead allows it to be controlled via this flag
    internal val activation: Activation = INACTIVE,
    internal val overlays: List<C> = emptyList() // FIXME consider List<Routing<C>>, but:
    //  overlay doesn't need identifier -- index position here is enough
    //  overlay + meta?
    //  overlay activation is same as that of the main element it's added to
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

@Parcelize
data class Routing<C : Parcelable>(
    val configuration: C,
    // differ will use this to match elements between two "back stack" states, instead of list position
    val identifier: Identifier = Identifier(),
    // unused for now, can be used with describing changes meaningful only to client code (e.g. configuration moved inside active window)
    val meta: Serializable = 0

) : Parcelable {

    @Parcelize
    data class Identifier(
        val id: Int = 0
    ) : Parcelable
}
