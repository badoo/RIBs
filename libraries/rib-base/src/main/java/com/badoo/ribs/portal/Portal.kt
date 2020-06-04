package com.badoo.ribs.portal

import android.os.Parcelable
import com.badoo.ribs.core.AncestryInfo
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import io.reactivex.Single

interface Portal : Rib {

    interface OtherSide {
        fun showContent(remoteNode: Node<*>, remoteConfiguration: Parcelable)
        fun showOverlay(remoteNode: Node<*>, remoteConfiguration: Parcelable)

        companion object {
            /**
             * For testing purposes only.
             */
            val NOOP = object : OtherSide {
                override fun showContent(remoteNode: Node<*>, remoteConfiguration: Parcelable) {}
                override fun showOverlay(remoteNode: Node<*>, remoteConfiguration: Parcelable) {}
            }
        }
    }

    interface Dependency {
        fun defaultRoutingAction(): (OtherSide) -> RoutingAction
        fun transitionHandler(): TransitionHandler<PortalRouter.Configuration>? = null
    }

    // Workflow
    fun showDefault(): Single<Rib>
    fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib>
}
