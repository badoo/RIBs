package com.badoo.ribs.samples.dialogs.dummy

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class DummyBuilder: SimpleBuilder<Dummy>() {

    override fun build(buildParams: BuildParams<Nothing?>): Dummy {
        val customisation = buildParams.getOrDefault(Dummy.Customisation())
        val interactor = DummyInteractor(buildParams = buildParams)
        return DummyNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor)
        )
    }
}
