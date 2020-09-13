package com.badoo.ribs.routing.state.changeset

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.state.action.single.ActionFactory
import com.badoo.ribs.routing.state.action.single.ActivateAction
import com.badoo.ribs.routing.state.action.single.AddAction
import com.badoo.ribs.routing.state.action.single.DeactivateAction
import com.badoo.ribs.routing.state.action.single.RemoveAction
import com.badoo.ribs.routing.state.action.single.ReversibleActionFactory
import com.badoo.ribs.routing.state.action.single.ReversibleActionPair
import com.badoo.ribs.routing.Routing

/**
 * Represents a command to change one or more [RoutingContext] elements.
 *
 * The command object holds only the data necessary to resolve the [RoutingContext] that the
 * command needs to be executed on on a logical level.
 *
 * Associated actions that need to be executed (resulting in [Resolution] and
 * [Node] manipulations) are to be found in the associated Actions created by [ActionFactory].
 */
internal sealed class RoutingCommand<C : Parcelable> {
    abstract val routing: Routing<C>
    abstract val actionFactory: ReversibleActionFactory

    data class Add<C : Parcelable>(
        override val routing: Routing<C>
    ) : RoutingCommand<C>() {
        override val actionFactory: ReversibleActionFactory =
            ReversibleActionPair.Factory(
                forwardActionFactory = AddAction.Factory,
                reverseActionFactory = RemoveAction.Factory
            )
    }

    data class Remove<C : Parcelable>(
        override val routing: Routing<C>
    ) : RoutingCommand<C>() {
        override val actionFactory: ReversibleActionFactory =
            ReversibleActionPair.Factory(
                forwardActionFactory = RemoveAction.Factory,
                reverseActionFactory = AddAction.Factory
            )
    }

    data class Activate<C : Parcelable>(
        override val routing: Routing<C>
    ) : RoutingCommand<C>() {

        override val actionFactory: ReversibleActionFactory =
            ReversibleActionPair.Factory(
                forwardActionFactory = ActivateAction.Factory,
                reverseActionFactory = DeactivateAction.Factory
            )
    }

    data class Deactivate<C : Parcelable>(
        override val routing: Routing<C>
    ) : RoutingCommand<C>() {

        override val actionFactory: ReversibleActionFactory =
            ReversibleActionPair.Factory(
                forwardActionFactory = DeactivateAction.Factory,
                reverseActionFactory = ActivateAction.Factory
            )
    }
}
