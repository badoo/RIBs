package com.badoo.ribs.samples.permissions.rib.parent.routing

import com.badoo.ribs.samples.permissions.rib.child.PermissionsSample
import com.badoo.ribs.samples.permissions.rib.child.PermissionsSampleBuilder
import com.badoo.ribs.samples.permissions.rib.parent.PermissionsSampleParent

internal open class PermissionsSampleParentChildBuilder(
    dependency: PermissionsSampleParent.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child: PermissionsSampleBuilder = PermissionsSampleBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: PermissionsSampleParent.Dependency
    ) : PermissionsSampleParent.Dependency by dependency,
        PermissionsSample.Dependency
}
