package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory

class LanguageSelectorNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<LanguageSelectorView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Nothing, LanguageSelector.Output> = NodeConnector()
) : Node<LanguageSelectorView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), LanguageSelector, Connectable<Nothing, LanguageSelector.Output> by connector