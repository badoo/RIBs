package com.badoo.ribs.sandbox.rib.foo_bar

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.view.ViewPlugin

class FooBarNode(
    viewFactory: ((ViewGroup) -> FooBarView?)?,
    interactor: FooBarInteractor,
    buildParams: BuildParams<*>,
    viewPlugins: Set<ViewPlugin>
) : Node<FooBarView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = null,
    interactor = interactor,
    viewPlugins = viewPlugins
), FooBar {

}
