package com.badoo.ribs.android.recyclerview.client

import com.badoo.ribs.core.routing.action.RoutingAction

interface RecyclerViewRibResolver<T> {

    fun resolve(element: T): RoutingAction
}
