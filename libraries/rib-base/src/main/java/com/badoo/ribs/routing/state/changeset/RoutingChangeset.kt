package com.badoo.ribs.routing.state.changeset

import android.os.Parcelable
import com.badoo.ribs.routing.Routing

internal typealias RoutingChangeset<C> = List<RoutingCommand<C>>

internal fun <C : Parcelable> RoutingChangeset<C>.addedOrRemoved(routing: Routing<C>): Boolean =
    any { it.routing == routing && (it is RoutingCommand.Add || it is RoutingCommand.Remove) }
