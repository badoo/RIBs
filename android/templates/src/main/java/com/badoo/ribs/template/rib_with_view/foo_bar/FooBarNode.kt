package com.badoo.ribs.template.rib_with_view.foo_bar

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.template.rib_with_view.foo_bar.feature.FooBarFeature
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class FooBarNode(
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    private val router: FooBarRouter,
    interactor: FooBarInteractor,
    private val input: ObservableSource<FooBar.Input>,
    private val output: Consumer<FooBar.Output>,
    private val feature: FooBarFeature
) : Node<FooBarView>(
    identifier = object : FooBar {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), FooBar.Workflow {

    /**
     * TODO:
     *  - use router / input / output / feature for FooBar.Workflow method implementations
     *  - remove them from constructor if they are not needed (don't forget to remove in FooBarModule, too)
     *  - do not use interactor directly! (instead, implement actions on children)
     */
}
