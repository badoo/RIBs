@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.comms_nodes_1.rib.container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerChildBuilders
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerRouter

class ContainerBuilder(
    private val dependency: Container.Dependency
) : SimpleBuilder<Container>() {

    private val builders by lazy { ContainerChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Container {

        val router = router(buildParams, builders)

        return ContainerNode(
            buildParams = BuildParams.Empty(),
            viewFactory = ContainerViewImpl.Factory().invoke(deps = null),
            plugins = listOfNotNull(router)
        )
    }

    private fun router(
        buildParams: BuildParams<Nothing?>,
        builders: ContainerChildBuilders
    ) = ContainerRouter(
        buildParams = buildParams,
        builders = builders
    )
}
