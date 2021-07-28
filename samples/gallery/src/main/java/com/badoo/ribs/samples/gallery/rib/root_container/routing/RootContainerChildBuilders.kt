package com.badoo.ribs.samples.gallery.rib.root_container.routing

import com.badoo.ribs.samples.gallery.rib.android_container.AndroidContainer
import com.badoo.ribs.samples.gallery.rib.android_container.AndroidContainerBuilder
import com.badoo.ribs.samples.gallery.rib.communication_container.CommunicationContainer
import com.badoo.ribs.samples.gallery.rib.communication_container.CommunicationContainerBuilder
import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainer
import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainerBuilder
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPickerBuilder
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainer
import com.badoo.ribs.samples.gallery.rib.routing_container.RoutingContainer
import com.badoo.ribs.samples.gallery.rib.routing_container.RoutingContainerBuilder

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
    {
    }
}



