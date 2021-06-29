package com.badoo.ribs.samples.permissions.rib.child2

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class PermissionsSampleChild2Builder(private val dependency: PermissionsSampleChild2.Dependency) :
    SimpleBuilder<PermissionsSampleChild2>() {

    override fun build(buildParams: BuildParams<Nothing?>): PermissionsSampleChild2 {
        val presenter = PermissionsSampleChild2PresenterImpl(dependency.permissionRequester)
        val viewDependencies = object : PermissionsSampleChild2View.Dependency {
            override val presenter: PermissionsSampleChild2Presenter = presenter
        }
        return PermissionsSampleChild2Node(
            buildParams = buildParams,
            viewFactory = PermissionsSampleChild2ViewImpl.Factory().invoke(viewDependencies),
            plugins = listOf(presenter)
        )
    }
}
