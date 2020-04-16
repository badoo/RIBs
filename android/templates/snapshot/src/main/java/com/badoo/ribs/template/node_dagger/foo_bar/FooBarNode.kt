package com.badoo.ribs.template.node_dagger.foo_bar

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.template.node_dagger.foo_bar.feature.FooBarFeature
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Consumer

class FooBarNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    private val router: FooBarRouter,
    private val input: ObservableSource<FooBar.Input>,
    private val output: Consumer<FooBar.Output>,
    private val feature: FooBarFeature,
    private val interactor: FooBarInteractor
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), FooBar.Workflow {

    /**
     * TODO:
     *  - use router / input / output / feature for FooBar.Workflow method implementations
     *  - remove them from constructor if they are not needed (don't forget to remove in FooBarModule, too)
     *  - keep in mind that in most cases you probably don't need to use interactor reference directly
     *      - its lifecycle methods are not accessible publicly (and it's good this way)
     *      - its internal consumers are usually reacting to children, and then it's better to
     *          trigger child workflows instead of faking them directly on the parent
     *  - as a general advice, try to trigger actions at points that are closest to where they would happen naturally,
     *      such that triggering involves executing all related actions (analytics, logging, etc)
     */

    override fun businessLogicOperation(): Single<FooBar.Workflow> =
        executeWorkflow {
            // todo e.g. push wish to feature / trigger input / output
            // feature.accept()
        }

    // todo: expose ALL possible children (even permanent parts), or remove if there's none
    // override fun attachChild1(): Single<Child.Workflow> =
    //     attachWorkflow {
    //         // router.push(ConfigurationForChild)
    //     }
}
