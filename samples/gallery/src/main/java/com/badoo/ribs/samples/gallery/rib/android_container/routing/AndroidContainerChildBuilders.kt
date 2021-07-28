package com.badoo.ribs.samples.gallery.rib.android_container.routing

import com.badoo.ribs.samples.gallery.rib.android_container.AndroidContainer
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPicker
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPickerBuilder

internal class AndroidContainerChildBuilders(
    dependency: AndroidContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = AndroidPickerBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: AndroidContainer.Dependency
    ) : AndroidContainer.Dependency by dependency,
        AndroidPicker.Dependency {
    }
}



