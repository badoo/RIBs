package com.badoo.ribs.template.rib_with_view.foo_bar

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.template.rib_with_view.foo_bar.feature.FooBarFeature
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Consumer

class FooBarNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    private val router: FooBarRouter,
    private val input: ObservableSource<FooBar.Input>,
    private val output: Consumer<FooBar.Output>,
    private val feature: FooBarFeature,
    interactor: FooBarInteractor // do NOT use interactor in workflows directly
) : Node<FooBarView>(
    savedInstanceState = savedInstanceState,
    identifier = object : FooBar {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), FooBar.Workflow {

    /**
     * TODO:
     *  - use router / input / output / feature for FooBar.Workflow method implementations
     *  - remove them from constructor if they are not needed (don't forget to remove in FooBarModule, too)
     *  - do NOT use interactor directly! (instead, implement actions on children)
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
