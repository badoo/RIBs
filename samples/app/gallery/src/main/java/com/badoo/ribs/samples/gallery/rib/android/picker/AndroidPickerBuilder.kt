package com.badoo.ribs.samples.gallery.rib.android.picker

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class AndroidPickerBuilder(
    private val dependency: AndroidPicker.Dependency
) : SimpleBuilder<AndroidPicker>() {

    override fun build(buildParams: BuildParams<Nothing?>): AndroidPicker {
        val customisation = buildParams.getOrDefault(AndroidPicker.Customisation())
        val interactor = interactor(buildParams)

        return node(buildParams, customisation, interactor)
    }

    private fun interactor(
        buildParams: BuildParams<*>,
    ) = AndroidPickerInteractor(
            buildParams = buildParams,
        )

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: AndroidPicker.Customisation,
        interactor: AndroidPickerInteractor
    ) = AndroidPickerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
}
