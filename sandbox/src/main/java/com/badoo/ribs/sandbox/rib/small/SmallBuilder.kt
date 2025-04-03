package com.badoo.ribs.sandbox.rib.small

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.sandbox.rib.small.routing.SmallChildBuilders
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration.Content

class SmallBuilder(
    private val dependency: Small.Dependency
) : SimpleBuilder<Small>() {

    private val builders by lazy { SmallChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Small {
        val backStack = BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )
        val interactor = interactor(
            buildParams = buildParams,
            backStack = backStack,
            portal = dependency.portal
        )
        val router = router(
            buildParams = buildParams,
            routingSource = backStack
        )
        return node(
            buildParams = buildParams,
            customisation = buildParams.getOrDefault(Small.Customisation()),
            plugins = listOf(
                router,
                interactor
            )
        )
    }

    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>,
        portal: Portal.OtherSide
    ): SmallInteractor =
        SmallInteractor(
            buildParams = buildParams,
            backStack = backStack,
            portal = portal
        )

    internal fun router(
        buildParams: BuildParams<Nothing?>,
        routingSource: RoutingSource<Configuration>
    ): SmallRouter =
        SmallRouter(
            buildParams = buildParams,
            routingSource = routingSource,
            builders = builders
        )

    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Small.Customisation,
        plugins: List<Plugin>
    ): SmallNode = SmallNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = plugins
    )
}
