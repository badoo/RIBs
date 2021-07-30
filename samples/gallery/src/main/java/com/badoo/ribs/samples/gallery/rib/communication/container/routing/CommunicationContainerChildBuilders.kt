package com.badoo.ribs.samples.gallery.rib.communication.container.routing

import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainer
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerBuilder
import com.badoo.ribs.samples.comms_nodes_1.rib.menu_example.MenuExample
import com.badoo.ribs.samples.comms_nodes_1.rib.menu_example.MenuExampleBuilder
import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPickerBuilder

internal class CommunicationContainerChildBuilders(
    dependency: CommunicationContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = CommunicationPickerBuilder(subtreeDeps)
    val greetingContainer = GreetingContainerBuilder(subtreeDeps)
    val menuExample = MenuExampleBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: CommunicationContainer.Dependency
    ) : CommunicationContainer.Dependency by dependency,
        CommunicationPicker.Dependency,
        GreetingContainer.Dependency,
        MenuExample.Dependency {
    }
}



