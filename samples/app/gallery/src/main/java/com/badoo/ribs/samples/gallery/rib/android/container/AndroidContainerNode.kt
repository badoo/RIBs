package com.badoo.ribs.samples.gallery.rib.android.container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer.Input
import com.badoo.ribs.samples.gallery.rib.android.container.AndroidContainer.Output

class AndroidContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<AndroidContainerView>,
    connector: NodeConnector<Input, Output> = NodeConnector(),
) : RxWorkflowNode<AndroidContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AndroidContainer, Connectable<Input, Output> by connector
