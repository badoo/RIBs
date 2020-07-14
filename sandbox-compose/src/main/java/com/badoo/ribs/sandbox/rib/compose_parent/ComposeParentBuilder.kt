@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentChildBuilders
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter

class ComposeParentBuilder(
    private val dependency: ComposeParent.Dependency
) : SimpleBuilder<ComposeParent>() {

    private val builders = ComposeParentChildBuilders(dependency)

    override fun build(buildParams: BuildParams<Nothing?>): ComposeParent {
        val customisation = buildParams.getOrDefault(ComposeParent.Customisation())
        val router = router(buildParams, customisation)

        return node(buildParams, customisation, listOf(router))
    }

    private fun router(
        buildParams: BuildParams<*>,
        customisation: ComposeParent.Customisation
    ) =
        ComposeParentRouter(
            buildParams = buildParams,
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
