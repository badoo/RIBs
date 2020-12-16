package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.template.node.foo_bar.FooBar.Input
import com.badoo.ribs.template.node.foo_bar.FooBar.Output
import io.reactivex.Single

class FooBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> FooBarView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), FooBar, Connectable<Input, Output> by connector {

    override fun businessLogicOperation(): Single<FooBar> =
        executeWorkflow {
            // todo e.g. push wish to feature / trigger input / output
            // feature.accept()
        }

    // todo: expose ALL possible children (even permanent parts), or remove if there's none
    // override fun attachChild1(): Single<Child> =
    //     attachWorkflow {
    //         // backStack.push(ConfigurationForChild)
    //     }
}
