package com.badoo.ribs.sandbox.rib.util

import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.test.emptyBuildParams
import com.nhaarman.mockitokotlin2.mock

class TestNode<V : RibView>(
    buildParams: BuildParams<*> = emptyBuildParams(),
    router: Router<*> = mock(),
    viewFactory: ViewFactory<V>? = mock(),
    interactor: Interactor<*, V> = mock()
) : Node<V>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = listOf(interactor, router)
) {

    var isAttached: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()
        isAttached = true
    }

    override fun onDestroy(isRecreating: Boolean) {
        super.onDestroy(isRecreating)
        isAttached = false
    }
}
