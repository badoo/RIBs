package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class RoutingPickerBuilder(
    private val dependency: RoutingPicker.Dependency
) : SimpleBuilder<RoutingPicker>() {

    override fun build(buildParams: BuildParams<Nothing?>): RoutingPicker {
        val customisation = buildParams.getOrDefault(RoutingPicker.Customisation())
        val interactor = interactor(buildParams)

        return node(buildParams, customisation, interactor)
    }

    private fun interactor(
        buildParams: BuildParams<*>,
    ) = RoutingPickerInteractor(
        buildParams = buildParams,
    )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: RoutingPicker.Customisation,
        interactor: RoutingPickerInteractor
    ) = RoutingPickerNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(
            interactor
        )
    )
}
