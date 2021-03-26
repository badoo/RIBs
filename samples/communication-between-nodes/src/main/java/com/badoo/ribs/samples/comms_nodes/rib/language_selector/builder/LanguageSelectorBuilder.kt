package com.badoo.ribs.samples.comms_nodes.rib.language_selector.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorPresenter
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorPresenterImpl
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorView

class LanguageSelectorBuilder : SimpleBuilder<Node<LanguageSelectorView>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<LanguageSelectorView> {
        val presenter = LanguageSelectorPresenterImpl()
        val viewDependency = object : LanguageSelectorView.Dependency {
            override val presenter: LanguageSelectorPresenter
                get() = presenter
        }

        return Node(
            buildParams = buildParams,
            viewFactory = LanguageSelector.Customisation().viewFactory.invoke(viewDependency),
            plugins = listOf(presenter)
        )
    }
}