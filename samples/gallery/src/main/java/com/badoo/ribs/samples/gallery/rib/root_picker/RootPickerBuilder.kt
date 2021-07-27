package com.badoo.ribs.samples.gallery.rib.root_picker

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams


class RootPickerBuilder(
    private val dependency: RootPicker.Dependency
) : SimpleBuilder<RootPicker>() {

    override fun build(buildParams: BuildParams<Nothing?>): RootPicker {
        val customisation = buildParams.getOrDefault(RootPicker.Customisation())

        return node(buildParams, customisation)
    }

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: RootPicker.Customisation
    ) = RootPickerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = emptyList()
        )
}
