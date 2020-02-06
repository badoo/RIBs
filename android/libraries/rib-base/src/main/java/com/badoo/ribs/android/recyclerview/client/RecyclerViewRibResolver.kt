package com.badoo.ribs.android.recyclerview.client

import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView

interface RecyclerViewRibResolver<T> {

    fun resolve(element: T): RoutingAction<RibView>
}
