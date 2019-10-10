package com.badoo.ribs.core.routing.configuration

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.portal.AncestryInfo
import kotlinx.android.parcel.Parcelize

/**
 * Describes a Configuration the [Node] can be in.
 *
 * [ConfigurationContext] can either be [ConfigurationContext.Unresolved], or
 * [ConfigurationContext.Resolved]
 */
internal sealed class ConfigurationContext<C : Parcelable> {

    abstract val configuration: C
    abstract val activationState: ActivationState

    /**
     * Represents the activation state a [ConfigurationContext] can be in.
     */
    enum class ActivationState {
        /**
         * Off-screen configurations are considered [INACTIVE].
         */
        INACTIVE,

        /**
         * Logically active, but technically off-screen configurations (e.g. screen is not
         * available during a save/restore cycle) are considered [SLEEPING].
         * Sleeping elements are to be restored to [ACTIVE] on the next wakeup.
         */
        SLEEPING,

        /**
         * On-screen configurations are considered [ACTIVE].
         */
        ACTIVE;

        /**
         * Transitions [ACTIVE] to [SLEEPING]. All other [ActivationState]s remain the same.
         */
        fun sleep(): ActivationState =
            when (this) {
                INACTIVE -> INACTIVE
                SLEEPING -> SLEEPING
                ACTIVE -> SLEEPING
            }

        /**
         * Transitions [SLEEPING] to [ACTIVE]. All other [ActivationState]s remain the same.
         */
        fun wakeUp(): ActivationState =
            when (this) {
                INACTIVE -> INACTIVE
                SLEEPING -> ACTIVE
                ACTIVE -> ACTIVE
            }
    }

    abstract fun sleep(): ConfigurationContext<C>
    abstract fun wakeUp(): ConfigurationContext<C>
    abstract fun resolve(
        resolver: (C) -> RoutingAction<*>,
        parentNode: Node<*>,
        onResolution: (Resolved<C>) -> Resolved<C>
    ): Resolved<C>

    /**
     * Represents [ConfigurationContext] that is persistable in a [android.os.Bundle],
     * storing the minimum amount of information from which a [Resolved] can be restored.
     */
    @Parcelize
    data class Unresolved<C : Parcelable>(
        override val activationState: ActivationState,
        override val configuration: C,
        val bundles: List<Bundle> = emptyList()
    ) : ConfigurationContext<C>(), Parcelable {

        init {
            // TODO add compile-time safety for this
            if (activationState == ACTIVE) {
                error("Unresolved elements cannot be ACTIVE")
            }
        }

        /**
         * Resolves and sets the associated [RoutingAction], builds associated [Node]s
         */
        override fun resolve(
            resolver: (C) -> RoutingAction<*>,
            parentNode: Node<*>,
            onResolution: (Resolved<C>) -> Resolved<C>
        ): Resolved<C> {
            bundles.forEach { it.classLoader = ConfigurationContext::class.java.classLoader }
            val routingAction = resolver.invoke(configuration)
            return onResolution.invoke(
                Resolved(
                    activationState = activationState,
                    configuration = configuration,
                    bundles = bundles,
                    routingAction = routingAction,
                    nodes = buildNodes(routingAction, parentNode)
                )
            )
        }

        private fun buildNodes(routingAction: RoutingAction<*>, parentNode: Node<*>): List<Node<*>> =
            routingAction.buildNodes(
                ancestryInfo = AncestryInfo.Child(
                    anchor = routingAction.anchor() ?: parentNode,
                    creatorConfiguration = configuration
                ),
                bundles = bundles
            )

        override fun sleep(): Unresolved<C> = copy(
            activationState = activationState.sleep()
        )

        override fun wakeUp(): Unresolved<C> = copy(
            activationState = activationState.wakeUp()
        )
    }

    /**
     * Represents [ConfigurationContext] that has its [RoutingAction] resolved, its [Node]s built
     * and attached to a parentNode.
     */
    data class Resolved<C : Parcelable>(
        override val activationState: ActivationState,
        override val configuration: C,
        var bundles: List<Bundle>,
        val routingAction: RoutingAction<*>,
        val nodes: List<Node<*>>
    ) : ConfigurationContext<C>() {

        override fun resolve(
            resolver: (C) -> RoutingAction<*>,
            parentNode: Node<*>,
            onResolution: (Resolved<C>) -> Resolved<C>
        ): Resolved<C> =
            this

        fun shrink() =
            Unresolved(
                activationState.sleep(),
                configuration,
                bundles
            )

        fun withActivationState(activationState: ActivationState) =
            copy(
                activationState = activationState
            )

        fun saveInstanceState() =
            copy(
                bundles = nodes.map { node ->
                    Bundle().also {
                        node.onSaveInstanceState(it)
                    }
                }
            )


        override fun sleep(): Resolved<C> =
            withActivationState(activationState.sleep())

        override fun wakeUp(): Resolved<C> =
            withActivationState(activationState.wakeUp())
    }
}
