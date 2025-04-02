package com.badoo.ribs.samples.gallery.rib.root.container.routing

import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer
import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainerBuilder
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainerBuilder
import com.badoo.ribs.samples.gallery.rib.other.container.OtherContainer
import com.badoo.ribs.samples.gallery.rib.other.container.OtherContainerBuilder
import com.badoo.ribs.samples.gallery.rib.root.container.RootContainer
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPicker
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPickerBuilder
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.RoutingContainer
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.RoutingContainerBuilder

internal class RootContainerChildBuilders(
    dependency: RootContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = RootPickerBuilder(subtreeDeps)
    val routingContainer = RoutingContainerBuilder(subtreeDeps)
    val communicationContainer = CommunicationContainerBuilder(subtreeDeps)
    val androidContainer = AndroidContainerBuilder(subtreeDeps)
    val otherContainer = OtherContainerBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: RootContainer.Dependency
    ) : RootContainer.Dependency by dependency,
        RootPicker.Dependency,
        RoutingContainer.Dependency,
        CommunicationContainer.Dependency,
        AndroidContainer.Dependency,
        OtherContainer.Dependency
}



