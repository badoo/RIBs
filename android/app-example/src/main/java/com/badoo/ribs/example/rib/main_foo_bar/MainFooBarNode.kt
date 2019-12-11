package com.badoo.ribs.example.rib.main_foo_bar

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewPlugin

class MainFooBarNode(
    viewFactory: ((ViewGroup) -> MainFooBarView?)?,
    private val router: MainFooBarRouter,
    interactor: MainFooBarInteractor,
    savedInstanceState: Bundle?,
    viewPlugins: Set<ViewPlugin>
) : Node<MainFooBarView>(
    savedInstanceState = savedInstanceState,
    identifier = object : MainFooBar {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor,
    viewPlugins = viewPlugins
), MainFooBar.Workflow {

}
