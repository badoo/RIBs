package com.badoo.ribs.core.routing.portal

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import io.reactivex.Single

interface Portal : Rib {

    interface OtherSide {
        fun showRemote(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable)
    }

    interface Dependency {
        fun defaultRoutingAction(): (Portal.OtherSide) -> RoutingAction<Nothing>
    }

    interface Workflow {
        fun showDefault(): Single<Node<*>>
        fun showInPortal(ancestryInfo: AncestryInfo): Single<Node<*>>
    }
}
