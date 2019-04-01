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
) {
    /**
     * Provides the router to the node
     *
     * Normally it is done via dependency injection, but here we create it directly,
     * so router should be exposed to set the instance to the node
     */
    internal fun provideRouter() = router
}
