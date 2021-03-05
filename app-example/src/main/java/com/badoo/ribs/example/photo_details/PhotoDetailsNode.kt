package com.badoo.ribs.example.photo_details

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.photo_details.PhotoDetails.Input
import com.badoo.ribs.example.photo_details.PhotoDetails.Output
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector

class PhotoDetailsNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<PhotoDetailsView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<PhotoDetailsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PhotoDetails, Connectable<Input, Output> by connector
