@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentChildBuilders
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration.Content

class ComposeParentBuilder(
    private val dependency: ComposeParent.Dependency
) : SimpleBuilder<ComposeParent>() {

    private val builders = ComposeParentChildBuilders(dependency)

    override fun build(buildParams: BuildParams<Nothing?>): ComposeParent {
        val customisation = buildParams.getOrDefault(ComposeParent.Customisation())
        val backStack: BackStack<Configuration> = BackStack(initialConfiguration = Content.ComposeLeaf(1), buildParams)
        val interactor = ComposeParentInteractor(buildParams, backStack)
        val router = router(buildParams, backStack, customisation)

        return node(buildParams, customisation, listOf(router, interactor))
    }

    private fun router(
        buildParams: BuildParams<*>,
        routingSource: RoutingSource<Configuration>,
        customisation: ComposeParent.Customisation,
    ): Router<Configuration> =
        ComposeParentRouter(
            buildParams = buildParams,
            routingSource = routingSource,
            builders = builders,
            transitionHandler = customisation.transitionHandler
        )

    private fun node(
        buildParams: BuildParams<*>,
        customisation: ComposeParent.Customisation,
        plugins: List<Plugin>
    ) = ComposeParentNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = plugins
    )
}
