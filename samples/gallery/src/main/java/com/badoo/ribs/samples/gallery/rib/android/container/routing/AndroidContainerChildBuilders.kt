package com.badoo.ribs.samples.gallery.rib.android.container.routing

import com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example.LaunchingActivitiesExample
import com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example.LaunchingActivitiesExampleBuilder
import com.badoo.ribs.samples.android.dialogs.rib.dialogs_example.DialogsExampleBuilder
import com.badoo.ribs.samples.android.dialogs.rib.dialogs_example.DialogsExample
import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPickerBuilder
import com.badoo.ribs.samples.android.permissions.rib.parent.PermissionsExample
import com.badoo.ribs.samples.android.permissions.rib.parent.PermissionsExampleBuilder

internal class AndroidContainerChildBuilders(
    dependency: AndroidContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = AndroidPickerBuilder(subtreeDeps)
    val activities = LaunchingActivitiesExampleBuilder(subtreeDeps)
    val permissions = PermissionsExampleBuilder(subtreeDeps)
    val dialogs = DialogsExampleBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: AndroidContainer.Dependency
    ) : AndroidContainer.Dependency by dependency,
        AndroidPicker.Dependency,
        LaunchingActivitiesExample.Dependency,
        PermissionsExample.Dependency,
        DialogsExample.Dependency {
    }
}



