package com.badoo.ribs.core.routing.portal

import android.os.Parcelable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import io.reactivex.Single

interface Portal : Rib<Nothing> {

    interface OtherSide {
        fun showContent(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable)
        fun showOverlay(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable)
    }

    interface Dependency {
        fun defaultRoutingAction(): (Portal.OtherSide) -> RoutingAction
        fun transitionHandler(): TransitionHandler<PortalRouter.Configuration>? = null
    }

    // Workflow
    fun showDefault(): Single<Rib<*>>
    fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib<*>>
}
