package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.ViewAware

internal class SimpleRoutingChild2Presenter : ViewAware<SimpleRoutingChild2View> {

    override fun onViewCreated(view: SimpleRoutingChild2View, viewLifecycle: Lifecycle) {
        view.showInitialMessage()
    }
}
