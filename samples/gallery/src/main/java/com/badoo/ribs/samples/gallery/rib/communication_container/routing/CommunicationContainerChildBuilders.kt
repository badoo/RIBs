package com.badoo.ribs.samples.gallery.rib.communication_container.routing

import com.badoo.ribs.samples.gallery.rib.communication_container.CommunicationContainer
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPicker
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPickerBuilder

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



