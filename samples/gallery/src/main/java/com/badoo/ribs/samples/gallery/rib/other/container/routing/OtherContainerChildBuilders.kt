package com.badoo.ribs.samples.gallery.rib.other.container.routing

import com.badoo.ribs.samples.gallery.rib.other.container.OtherContainer
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPicker
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPickerBuilder
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExample
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleBuilder

internal class OtherContainerChildBuilders(
    dependency: OtherContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = OtherPickerBuilder(subtreeDeps)
    val retainedInstanceExample = RetainedInstanceExampleBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: OtherContainer.Dependency
    ) : OtherContainer.Dependency by dependency,
        OtherPicker.Dependency,
        RetainedInstanceExample.Dependency {
    }
}



