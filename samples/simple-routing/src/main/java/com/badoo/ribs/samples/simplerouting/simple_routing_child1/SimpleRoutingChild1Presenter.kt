package com.badoo.ribs.samples.simplerouting.simple_routing_child1

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.ViewAware

internal class SimpleRoutingChild1Presenter(
    private val title: String
) : ViewAware<SimpleRoutingChild1View> {

    override fun onViewCreated(view: SimpleRoutingChild1View, viewLifecycle: Lifecycle) {
        view.setTitle(title)
    }
}
