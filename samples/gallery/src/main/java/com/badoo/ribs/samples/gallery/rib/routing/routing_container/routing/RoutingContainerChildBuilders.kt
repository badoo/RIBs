package com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing

import com.badoo.ribs.samples.gallery.rib.routing.routing_container.RoutingContainer
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPickerBuilder
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.builder.SimpleRoutingParentBuilder

internal class RoutingContainerChildBuilders(
    dependency: RoutingContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = RoutingPickerBuilder(subtreeDeps)
    val simpleRouting = SimpleRoutingParentBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: RoutingContainer.Dependency
    ) : RoutingContainer.Dependency by dependency,
        RoutingPicker.Dependency,
        SimpleRoutingParent.Dependency {
    }
}



