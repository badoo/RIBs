package com.badoo.ribs.samples.gallery.rib.root.picker

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams


class RootPickerBuilder(
    private val dependency: RootPicker.Dependency
) : SimpleBuilder<RootPicker>() {

    override fun build(buildParams: BuildParams<Nothing?>): RootPicker {
        val customisation = buildParams.getOrDefault(RootPicker.Customisation())
        val interactor = interactor(buildParams)

        return node(buildParams, customisation, interactor)
    }

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
    ) = RootPickerInteractor(buildParams)

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: RootPicker.Customisation,
        interactor: RootPickerInteractor
    ) = RootPickerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
}
