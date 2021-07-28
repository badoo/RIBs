package com.badoo.ribs.samples.gallery.rib.routing_container.routing

import com.badoo.ribs.samples.gallery.rib.routing_container.RoutingContainer
import com.badoo.ribs.samples.gallery.rib.routing_picker.RoutingPicker
import com.badoo.ribs.samples.gallery.rib.routing_picker.RoutingPickerBuilder

internal class RoutingContainerChildBuilders(
    dependency: RoutingContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = RoutingPickerBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: RoutingContainer.Dependency
    ) : RoutingContainer.Dependency by dependency,
        RoutingPicker.Dependency {
    }
}



