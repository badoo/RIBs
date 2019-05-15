package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.action.AddAction
import kotlinx.android.parcel.Parcelize

internal sealed class ConfigurationContext<C : Parcelable> {

    abstract val activationState: ActivationState

    enum class ActivationState {
        INACTIVE, SLEEPING, ACTIVE;

        fun sleep(): ActivationState =
            when (this) {
                INACTIVE -> INACTIVE
                SLEEPING -> SLEEPING
                ACTIVE -> SLEEPING
            }

        fun wakeUp(): ActivationState =
            when (this) {
                INACTIVE -> INACTIVE
                SLEEPING -> ACTIVE
                ACTIVE -> ACTIVE
            }
    }

    abstract fun sleep(): ConfigurationContext<C>
    abstract fun wakeUp(): ConfigurationContext<C>

    @Parcelize
    data class Unresolved<C : Parcelable>(
        val configuration: C,
        val bundles: List<Bundle> = emptyList(),
        override val activationState: ActivationState = INACTIVE
    ) : ConfigurationContext<C>(), Parcelable {

        fun resolve(resolver: (C) -> RoutingAction<*>, parentNode: Node<*>): Resolved<C> {
            val routingAction = resolver.invoke(configuration)

            return Resolved(
                configuration = configuration,
                routingAction = routingAction,
                nodes = routingAction.buildNodes(),
                bundles = emptyList(),
                activationState = INACTIVE
            ).also {
                AddAction.execute(it, parentNode)
            }
        }

        override fun sleep(): Unresolved<C> = copy(
            activationState = activationState.sleep()
        )

        override fun wakeUp(): Unresolved<C> = copy(
            activationState = activationState.wakeUp()
        )
    }

    data class Resolved<C : Parcelable>(
        val configuration: C,
        var bundles: List<Bundle> = emptyList(),
        val routingAction: RoutingAction<*>,
        val nodes: List<Node.Descriptor>,
        override val activationState: ActivationState
    ) : ConfigurationContext<C>() {

        fun shrink() = 
            Unresolved(configuration, bundles, activationState)

        fun withActivationState(activationState: ActivationState) =
            copy(
                activationState = activationState
            )

        override fun sleep(): Resolved<C> =
            withActivationState(activationState.sleep())

        override fun wakeUp(): Resolved<C> =
            withActivationState(activationState.wakeUp())
    }
}
