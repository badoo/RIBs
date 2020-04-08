package com.badoo.ribs.example.rib.foo_bar

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewPlugin

class FooBarNode(
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    interactor: FooBarInteractor,
    savedInstanceState: Bundle?,
    viewPlugins: Set<ViewPlugin>
) : Node<FooBarView>(
    savedInstanceState = savedInstanceState,
    identifier = object : FooBar {},
    viewFactory = viewFactory,
    router = null,
    interactor = interactor,
    viewPlugins = viewPlugins
), FooBar.Workflow {

}
