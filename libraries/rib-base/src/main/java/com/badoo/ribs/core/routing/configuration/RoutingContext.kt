package com.badoo.ribs.core.routing.configuration

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.annotation.OutdatedDocumentation
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.util.RIBs
import kotlinx.android.parcel.Parcelize

/**
 * Describes a Configuration the [Node] can be in.
 *
 * [RoutingContext] can either be [RoutingContext.Unresolved], or
 * [RoutingContext.Resolved]
 */
@OutdatedDocumentation
internal sealed class RoutingContext<C : Parcelable> {

    abstract val routing: Routing<C>
    abstract val activationState: ActivationState

    /**
     * Represents the activation state a [RoutingContext] can be in.
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

    abstract fun withActivationState(activationState: ActivationState): RoutingContext<C>
    abstract fun sleep(): RoutingContext<C>
    abstract fun wakeUp(): RoutingContext<C>
    abstract fun shrink(): Unresolved<C>
    abstract fun resolve(resolver: RoutingResolver<C>, parentNode: Node<*>): Resolved<C>

    /**
     * Represents [RoutingContext] that is persistable in a [android.os.Bundle],
     * storing the minimum amount of information from which a [Resolved] can be restored.
     */
    @Parcelize
    data class Unresolved<C : Parcelable>(
        override val activationState: ActivationState,
        override val routing: Routing<C>,
        val bundles: List<Bundle> = emptyList()
    ) : RoutingContext<C>(), Parcelable {

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
            resolver: RoutingResolver<C>,
            parentNode: Node<*>
        ): Resolved<C> {
            bundles.forEach { it.classLoader = RoutingContext::class.java.classLoader }
            val routingAction = resolver.resolve(routing)
            return Resolved(
                    activationState = activationState,
                    routing = routing,
                    bundles = bundles,
                    routingAction = routingAction,
                    nodes = buildNodes(routingAction, parentNode)
                )
        }

        override fun shrink(): Unresolved<C> =
            this

        private fun buildNodes(routingAction: RoutingAction, parentNode: Node<*>): List<Node<*>> {
            if (bundles.isNotEmpty() && bundles.size != routingAction.nbNodesToBuild) {
                RIBs.errorHandler.handleNonFatalError(
                    "Bundles size ${bundles.size} don't match expected nodes count ${routingAction.nbNodesToBuild}"
                )
            }
            val template = createBuildContext(routingAction, parentNode)
            val buildContexts = List(routingAction.nbNodesToBuild) {
                template.copy(
                    savedInstanceState = bundles.getOrNull(it)
                )
            }

            return routingAction.buildNodes(
                buildContexts = buildContexts
            ).map { it.node }
        }

        private fun createBuildContext(
            routingAction: RoutingAction,
            parentNode: Node<*>
        ): BuildContext = BuildContext(
            ancestryInfo = AncestryInfo.Child(
                anchor = routingAction.anchor() ?: parentNode,
                creatorConfiguration = routing
            ),
            savedInstanceState = null,
            customisations = parentNode.buildContext.customisations.getSubDirectoryOrSelf(
                parentNode::class
            )
        )

        override fun withActivationState(activationState: ActivationState) =
            copy(
                activationState = activationState
            )

        override fun sleep(): Unresolved<C> = copy(
            activationState = activationState.sleep()
        )

        override fun wakeUp(): Unresolved<C> = copy(
            activationState = activationState.wakeUp()
        )
    }

    /**
     * Represents [RoutingContext] that has its [RoutingAction] resolved, its [Node]s built
     * and attached to a parentNode.
     */
    data class Resolved<C : Parcelable>(
        override val activationState: ActivationState,
        override val routing: Routing<C>,
        var bundles: List<Bundle>,
        val routingAction: RoutingAction,
        val nodes: List<Node<*>>
    ) : RoutingContext<C>() {

        override fun resolve(
            resolver: RoutingResolver<C>,
            parentNode: Node<*>
        ): Resolved<C> = this

        override fun shrink(): Unresolved<C> =
            Unresolved(
                activationState.sleep(),
                routing,
                bundles
            )

        fun saveInstanceState() =
            copy(
                bundles = nodes.map { node ->
                    Bundle().also {
                        node.onSaveInstanceState(it)
                    }
                }
            )

        override fun withActivationState(activationState: ActivationState) =
            copy(
                activationState = activationState
            )

        override fun sleep(): Resolved<C> =
            withActivationState(activationState.sleep())

        override fun wakeUp(): Resolved<C> =
            withActivationState(activationState.wakeUp())
    }
}
