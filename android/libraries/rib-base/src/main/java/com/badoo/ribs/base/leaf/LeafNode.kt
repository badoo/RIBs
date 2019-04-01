package com.badoo.ribs.base.leaf

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.uber.rib.util.RibRefWatcher

class LeafNode<V: RibView>(
    identifier: Rib,
    viewFactory: ViewFactory<V>?,
    interactor: LeafInteractor<V>,
    ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
): Node<V>(
    identifier = identifier,
    viewFactory = viewFactory,
    router = interactor.provideRouter(),
    interactor = interactor,
    ribRefWatcher = ribRefWatcher
)
