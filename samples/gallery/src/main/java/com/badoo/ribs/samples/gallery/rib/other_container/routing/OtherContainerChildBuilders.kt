package com.badoo.ribs.samples.gallery.rib.other_container.routing

import com.badoo.ribs.samples.gallery.rib.other_container.OtherContainer
import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPicker
import com.badoo.ribs.samples.gallery.rib.other_picker.OtherPickerBuilder

internal class OtherContainerChildBuilders(
    dependency: OtherContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = OtherPickerBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: OtherContainer.Dependency
    ) : OtherContainer.Dependency by dependency,
        OtherPicker.Dependency {
    }
}



