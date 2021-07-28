package com.badoo.ribs.samples.gallery.rib.android.container.routing

import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPickerBuilder

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



