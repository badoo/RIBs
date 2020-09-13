package com.badoo.ribs.sandbox.rib.small

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.sandbox.rib.small.routing.SmallChildBuilders
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter

class SmallBuilder(
    private val dependency: Small.Dependency
) : SimpleBuilder<Small>() {

    private val builders by lazy { SmallChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Small {
        val interactor = interactor(
            buildParams = buildParams,
            portal = dependency.portal()
        )
        val router = router(
            buildParams = buildParams,
            interactor = interactor
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
        portal: Portal.OtherSide
    ): SmallInteractor =
        SmallInteractor(
            buildParams = buildParams,
            portal = portal
        )

    internal fun router(
        buildParams: BuildParams<Nothing?>,
        interactor: SmallInteractor
    ): SmallRouter =
        SmallRouter(
            buildParams = buildParams,
            routingSource = interactor,
            builders = builders
        )

    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Small.Customisation,
        plugins: List<Plugin>
    ) : SmallNode = SmallNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = plugins
    )
}
