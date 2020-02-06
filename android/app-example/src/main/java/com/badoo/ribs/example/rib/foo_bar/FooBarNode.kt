package com.badoo.ribs.example.rib.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewPlugin

class FooBarNode(
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    private val router: FooBarRouter,
    interactor: FooBarInteractor,
    buildParams: BuildParams<*>,
    viewPlugins: Set<ViewPlugin>
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor,
    viewPlugins = viewPlugins
), FooBar.Workflow {

}
