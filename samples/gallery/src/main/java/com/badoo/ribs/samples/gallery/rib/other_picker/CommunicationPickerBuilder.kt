package com.badoo.ribs.samples.gallery.rib.other_picker

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class OtherPickerBuilder(
    private val dependency: OtherPicker.Dependency
) : SimpleBuilder<OtherPicker>() {

    override fun build(buildParams: BuildParams<Nothing?>): OtherPicker {
        val customisation = buildParams.getOrDefault(OtherPicker.Customisation())
        val interactor = interactor(buildParams)

        return node(buildParams, customisation, interactor)
    }

    private fun interactor(
        buildParams: BuildParams<*>,
    ) = OtherPickerInteractor(
            buildParams = buildParams,
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: OtherPicker.Customisation,
        interactor: OtherPickerInteractor
    ) = OtherPickerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
}
