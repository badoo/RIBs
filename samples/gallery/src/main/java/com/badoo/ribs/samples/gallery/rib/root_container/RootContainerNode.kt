package com.badoo.ribs.samples.gallery.rib.root_container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPicker
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainer.Input
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainer.Output
import com.badoo.ribs.samples.gallery.rib.root_container.routing.RootContainerRouter.Configuration
import io.reactivex.Single

class RootContainerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<RootContainerView>?,
    private val backStack: BackStack<Configuration>,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<RootContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), RootContainer, Connectable<Input, Output> by connector {

    override fun attachRootPicker(): Single<RootPicker> =
        attachWorkflow {
            backStack.push(Configuration.Picker)
        }
}
