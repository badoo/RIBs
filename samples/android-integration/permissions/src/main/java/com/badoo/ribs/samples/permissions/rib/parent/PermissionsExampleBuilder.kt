package com.badoo.ribs.samples.permissions.rib.parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsExampleChildBuilder
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsExampleRouter

class PermissionsExampleBuilder(
    private val dependency: PermissionsExample.Dependency
) : SimpleBuilder<PermissionsExample>() {

    override fun build(buildParams: BuildParams<Nothing?>): PermissionsExample {
        val router = PermissionsExampleRouter(
            buildParams = buildParams,
            builders = PermissionsExampleChildBuilder(dependency)
        )

        return PermissionsExampleNode(
            buildParams = buildParams,
            viewFactory = PermissionsExampleViewImpl.Factory().invoke(null),
            plugins = listOf(router)
        )
    }
}
