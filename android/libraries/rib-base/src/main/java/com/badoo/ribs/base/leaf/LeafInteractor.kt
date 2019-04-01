package com.badoo.ribs.base.leaf

import com.badoo.ribs.base.leaf.LeafRouter.Configuration
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.view.RibView
import io.reactivex.disposables.Disposable

abstract class LeafInteractor<V: RibView>(
    disposables: Disposable?
): Interactor<Configuration, V>(
    disposables = disposables,
    router = LeafRouter()
)
