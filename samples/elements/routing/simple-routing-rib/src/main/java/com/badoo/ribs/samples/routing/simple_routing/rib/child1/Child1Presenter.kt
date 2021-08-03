package com.badoo.ribs.samples.routing.simple_routing.rib.child1

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.ViewAware

internal class Child1Presenter(
    private val title: String
) : ViewAware<Child1View> {

    override fun onViewCreated(view: Child1View, viewLifecycle: Lifecycle) {
        view.setTitle(title)
    }
}
