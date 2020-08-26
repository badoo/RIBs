package com.badoo.ribs.portal

import android.os.Parcelable
import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.action.Resolution
import com.badoo.ribs.routing.transition.handler.TransitionHandler

@ExperimentalApi
interface Portal : Rib {

    @ExperimentalApi
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

    @ExperimentalApi
    interface Dependency {
        fun defaultRoutingAction(): (OtherSide) -> Resolution
        fun transitionHandler(): TransitionHandler<PortalRouter.Configuration>? = null
    }
}
