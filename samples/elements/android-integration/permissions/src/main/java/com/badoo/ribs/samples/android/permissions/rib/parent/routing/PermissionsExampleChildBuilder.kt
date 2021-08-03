package com.badoo.ribs.samples.android.permissions.rib.parent.routing

import com.badoo.ribs.samples.android.permissions.rib.child1.PermissionsSampleChild1
import com.badoo.ribs.samples.android.permissions.rib.child1.PermissionsSampleChild1Builder
import com.badoo.ribs.samples.android.permissions.rib.child2.PermissionsSampleChild2
import com.badoo.ribs.samples.android.permissions.rib.child2.PermissionsSampleChild2Builder
import com.badoo.ribs.samples.android.permissions.rib.parent.PermissionsExample

internal open class PermissionsExampleChildBuilder(
    dependency: PermissionsExample.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child1Builder: PermissionsSampleChild1Builder = PermissionsSampleChild1Builder(subtreeDeps)
    val child2Builder: PermissionsSampleChild2Builder = PermissionsSampleChild2Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: PermissionsExample.Dependency
    ) : PermissionsExample.Dependency by dependency,
        PermissionsSampleChild2.Dependency, PermissionsSampleChild1.Dependency
}
