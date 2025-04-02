package com.badoo.ribs.example.feed_container

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.feed_container.FeedContainer.Input
import com.badoo.ribs.example.feed_container.FeedContainer.Output
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector

class FeedContainerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<FeedContainerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<FeedContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FeedContainer, Connectable<Input, Output> by connector
