package com.badoo.ribs.samples.permissions.rib.parent.routing

import com.badoo.ribs.samples.permissions.rib.child1.PermissionsSampleChild1
import com.badoo.ribs.samples.permissions.rib.child1.PermissionsSampleChild1Builder
import com.badoo.ribs.samples.permissions.rib.child2.PermissionsSampleChild2
import com.badoo.ribs.samples.permissions.rib.child2.PermissionsSampleChild2Builder
import com.badoo.ribs.samples.permissions.rib.parent.PermissionsSampleParent

internal open class PermissionsSampleParentChildBuilder(
    dependency: PermissionsSampleParent.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val child1Builder = PermissionsSampleChild1Builder(subtreeDeps)
    val child2Builder = PermissionsSampleChild2Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: PermissionsSampleParent.Dependency
    ) : PermissionsSampleParent.Dependency by dependency,
        PermissionsSampleChild2.Dependency, PermissionsSampleChild1.Dependency
}
