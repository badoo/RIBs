package com.badoo.ribs.samples.dialogs.rib.dummy

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class DummyBuilder : SimpleBuilder<Dummy>() {

    override fun build(buildParams: BuildParams<Nothing?>): Dummy {
        val presenter = DummyPresenterImpl()
        val viewDependencies = object : DummyView.Dependency {
            override val presenter: DummyPresenter = presenter
        }
        return DummyNode(
            buildParams = buildParams,
            viewFactory = DummyViewImpl.Factory().invoke(viewDependencies),
            plugins = listOf(presenter)
        )
    }
}
