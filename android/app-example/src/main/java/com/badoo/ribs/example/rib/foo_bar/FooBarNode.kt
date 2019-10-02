package com.badoo.ribs.example.rib.foo_bar

import com.badoo.ribs.core.BuildContext
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewPlugin

class FooBarNode(
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    private val router: FooBarRouter,
    interactor: FooBarInteractor,
    buildContext: BuildContext.Resolved<*>,
    viewPlugins: Set<ViewPlugin>
) : Node<FooBarView>(
    buildContext = buildContext,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor,
    viewPlugins = viewPlugins
), FooBar.Workflow {

}
