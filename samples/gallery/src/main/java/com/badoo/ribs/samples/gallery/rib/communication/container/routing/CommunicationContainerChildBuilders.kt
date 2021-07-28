package com.badoo.ribs.samples.gallery.rib.communication.container.routing

import com.badoo.ribs.samples.gallery.rib.communication.container.CommunicationContainer
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPickerBuilder

internal class CommunicationContainerChildBuilders(
    dependency: CommunicationContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = CommunicationPickerBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: CommunicationContainer.Dependency
    ) : CommunicationContainer.Dependency by dependency,
        CommunicationPicker.Dependency {
    }
}



