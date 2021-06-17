package com.badoo.ribs.samples.permissions.rib.parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsSampleParentChildBuilder
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsSampleParentRouter

class PermissionsSampleParentBuilder(private val dependency: PermissionsSampleParent.Dependency) :
    SimpleBuilder<PermissionsSampleParent>() {

    override fun build(buildParams: BuildParams<Nothing?>): PermissionsSampleParent {
        val router = PermissionsSampleParentRouter(
            buildParams = buildParams,
            builders = PermissionsSampleParentChildBuilder(dependency)
        )

        return PermissionsSampleParentNode(
            buildParams = buildParams,
            viewFactory = PermissionsSampleParentViewImpl.Factory().invoke(null),
            plugins = listOf(router)
        )
    }
}
