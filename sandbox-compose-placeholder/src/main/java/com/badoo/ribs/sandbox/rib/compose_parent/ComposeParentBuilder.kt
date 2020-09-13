@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin

class ComposeParentBuilder(
    private val dependency: ComposeParent.Dependency
) : SimpleBuilder<ComposeParent>() {

    override fun build(buildParams: BuildParams<Nothing?>): ComposeParent {
        val customisation = buildParams.getOrDefault(ComposeParent.Customisation())

        return node(buildParams, customisation)
    }

    private fun node(
        buildParams: BuildParams<*>,
        customisation: ComposeParent.Customisation
    ) = ComposeParentNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = emptyList()
    )
}
