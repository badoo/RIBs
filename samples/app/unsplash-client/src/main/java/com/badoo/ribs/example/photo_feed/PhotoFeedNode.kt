package com.badoo.ribs.example.photo_feed

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.photo_feed.PhotoFeed.Input
import com.badoo.ribs.example.photo_feed.PhotoFeed.Output
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView

class PhotoFeedNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<PhotoFeedView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<PhotoFeedView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PhotoFeed, Connectable<Input, Output> by connector
