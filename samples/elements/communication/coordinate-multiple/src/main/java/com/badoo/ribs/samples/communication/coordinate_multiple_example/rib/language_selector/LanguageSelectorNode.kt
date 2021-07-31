package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.LanguageSelector.Output

class LanguageSelectorNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<LanguageSelectorView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Nothing, Output> = NodeConnector()
) : Node<LanguageSelectorView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), LanguageSelector, Connectable<Nothing, Output> by connector
