package com.badoo.ribs.example.component.app_bar

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.example.component.app_bar.AppBar.Input
import com.badoo.ribs.example.component.app_bar.AppBar.Output
import io.reactivex.Single

class AppBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> AppBarView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<AppBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AppBar, Connectable<Input, Output> by connector {

    /**
     * TODO:
     *  - use router / input / output / feature for AppBar method implementations
     *  - keep in mind that in most cases you probably don't need to use interactor reference directly
     *      - its lifecycle methods are not accessible publicly (and it's good this way)
     *      - its internal consumers are usually reacting to children, and then it's better to
     *          trigger child workflows instead of faking them directly on the parent
     *  - as a general advice, try to trigger actions at points that are closest to where they would happen naturally,
     *      such that triggering involves executing all related actions (analytics, logging, etc)
     */

    override fun businessLogicOperation(): Single<AppBar> =
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
