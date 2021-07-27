package com.badoo.ribs.samples.gallery.rib.communication_picker

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class CommunicationPickerBuilder(
    private val dependency: CommunicationPicker.Dependency
) : SimpleBuilder<CommunicationPicker>() {

    override fun build(buildParams: BuildParams<Nothing?>): CommunicationPicker {
        val customisation = buildParams.getOrDefault(CommunicationPicker.Customisation())
        val interactor = interactor(buildParams)

        return node(buildParams, customisation, interactor)
    }

    private fun interactor(
        buildParams: BuildParams<*>,
    ) = CommunicationPickerInteractor(
            buildParams = buildParams,
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: CommunicationPicker.Customisation,
        interactor: CommunicationPickerInteractor
    ) = CommunicationPickerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
}
