package com.badoo.ribs.samples.routing.simple_routing.rib.child2

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.ViewAware

internal class Child2Presenter : ViewAware<Child2View> {

    override fun onViewCreated(view: Child2View, viewLifecycle: Lifecycle) {
        view.showInitialMessage()
    }
}
