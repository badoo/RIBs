package com.badoo.ribs.samples.permissions.rib

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class PermissionsBuilder(private val dependency: PermissionsRib.Dependency) :
        SimpleBuilder<PermissionsRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): PermissionsRib {
        val presenter = PermissionsPresenterImpl(dependency.permissionRequester)
        val viewDependencies = object : PermissionsView.Dependency {
            override val presenter: PermissionsPresenter = presenter
        }
        return PermissionsNode(
                buildParams = buildParams,
                viewFactory = PermissionsViewImpl.Factory().invoke(viewDependencies),
                plugins = listOf(presenter)
        )
    }
}
