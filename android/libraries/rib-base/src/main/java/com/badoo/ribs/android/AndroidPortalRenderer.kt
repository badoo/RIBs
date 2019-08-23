package com.badoo.ribs.android

import android.view.View
import android.view.ViewGroup
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Portal.Source.Model.Showing

class AndroidPortalRenderer(
    private val portalContainer: ViewGroup,
    private val regularContentContainer: ViewGroup
) : Portal.Renderer {

    override fun accept(portalVm: Portal.Source.Model) {
        regularContentContainer.visibility = when (portalVm) {
            is Showing -> View.GONE
            else -> View.VISIBLE
        }

        if (portalVm is Showing) {
            portalVm.node.attachToView(portalContainer)
        }
    }
}
