@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.comms_nodes_1.rib.container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerChildBuilders
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerRouter
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerRouter.Configuration
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerRouter.Configuration.Content

class ContainerBuilder(
    private val dependency: Container.Dependency
) : SimpleBuilder<Container>() {

    private val builders by lazy { ContainerChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Container {
        val backStack = backStack(buildParams)
        val router = router(buildParams, backStack, builders)
        val presenter = ContainerPresenterImpl(backStack)

        return ContainerNode(
            buildParams = BuildParams.Empty(),
            viewFactory = ContainerViewImpl.Factory().invoke(deps = null),
            plugins = listOfNotNull(router, presenter)
        )
    }

    private fun backStack(buildParams: BuildParams<Nothing?>): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Content.Child1
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>,
        builders: ContainerChildBuilders
    ) = ContainerRouter(
        buildParams = buildParams,
        builders = builders,
        routingSource = backStack
    )
}
