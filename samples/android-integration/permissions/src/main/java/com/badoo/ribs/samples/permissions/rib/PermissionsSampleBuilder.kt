package com.badoo.ribs.samples.permissions.rib

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class PermissionsSampleBuilder(private val dependency: PermissionsSample.Dependency) :
    SimpleBuilder<PermissionsSample>() {

    override fun build(buildParams: BuildParams<Nothing?>): PermissionsSample {
        val presenter = PermissionsSamplePresenterImpl(dependency.permissionRequester)
        val viewDependencies = object : PermissionsView.Dependency {
            override val presenter: PermissionsSamplePresenter = presenter
        }
        return PermissionsSampleNode(
            buildParams = buildParams,
            viewFactory = PermissionsViewImpl.Factory().invoke(viewDependencies),
            plugins = listOf(presenter)
        )
    }
}
