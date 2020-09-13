package com.badoo.ribs.example.photo_details

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.example.photo_details.PhotoDetails.Input
import com.badoo.ribs.example.photo_details.PhotoDetails.Output

class PhotoDetailsNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> PhotoDetailsView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<PhotoDetailsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), PhotoDetails, Connectable<Input, Output> by connector
