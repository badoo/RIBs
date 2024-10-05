package com.badoo.ribs.template.leaf.foo_bar.v1

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.template.leaf.foo_bar.common.FooBar.Input
import com.badoo.ribs.template.leaf.foo_bar.common.FooBar.Output
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import io.reactivex.Single

class FooBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<FooBarView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FooBarRib, Connectable<Input, Output> by connector {

    override fun businessLogicOperation(): Single<FooBarRib> =
        executeWorkflow {
            // todo e.g. push wish to feature / trigger input / output
            // feature.accept()
        }
}
