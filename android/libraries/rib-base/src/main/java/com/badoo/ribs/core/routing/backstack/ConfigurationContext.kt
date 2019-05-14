package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

internal sealed class ConfigurationContext<C : Parcelable> {

    abstract val activationState: ActivationState

    enum class ActivationState { INACTIVE, SLEEPING, ACTIVE }

    @Parcelize
    data class Unresolved<C : Parcelable>(
        val configuration: C,
        val bundles: List<Bundle> = emptyList(),
        override val activationState: ActivationState = INACTIVE
    ) : ConfigurationContext<C>(), Parcelable

    data class Resolved<C : Parcelable>(
        val configuration: C,
        var bundles: List<Bundle> = emptyList(),
        val routingAction: RoutingAction<*>,
        val builtNodes: List<Node.Descriptor>,
        override val activationState: ActivationState
    ) : ConfigurationContext<C>() {

        fun shrink() = 
            Unresolved(configuration, bundles, activationState)
    }
}
