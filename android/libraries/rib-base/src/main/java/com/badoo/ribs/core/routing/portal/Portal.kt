package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.action.RoutingAction
import io.reactivex.Single

interface Portal : Rib {

    interface Sink {
        fun showRemote(ancestryInfo: AncestryInfo)
    }

    interface Dependency {
        fun defaultRoutingAction(): (Portal.Sink) -> RoutingAction<Nothing>
    }

    interface Workflow {
        fun showDefault(): Single<Node<*>>
        fun showInPortal(ancestryInfo: AncestryInfo): Single<Node<*>>
    }
}
