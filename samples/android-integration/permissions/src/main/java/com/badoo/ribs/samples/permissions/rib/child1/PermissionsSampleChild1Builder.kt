package com.badoo.ribs.samples.permissions.rib.child1

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.permissions.rib.child2.PermissionsSampleChild2Presenter

class PermissionsSampleChild1Builder(private val dependency: PermissionsSampleChild1.Dependency) :
    SimpleBuilder<PermissionsSampleChild1>() {

    override fun build(buildParams: BuildParams<Nothing?>): PermissionsSampleChild1 {
        val presenter = PermissionsSampleChild1PresenterImpl(dependency.permissionRequester)
        val viewDependencies = object : PermissionsSampleChild1View.Dependency {
            override val presenter: PermissionsSampleChild2Presenter = presenter
        }
        return PermissionsSampleChild1Node(
            buildParams = buildParams,
            viewFactory = PermissionsSampleChild1ViewImpl.Factory().invoke(viewDependencies),
            plugins = listOf(presenter)
        )
    }
}
