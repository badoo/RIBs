package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.routing.router.Router

class TestNode<V : RibView>(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactoryBuilder<Nothing?, V>,
    private val router: Router<*>,
    interactor: Interactor<*, V>
) : Node<V>(
    buildParams = buildParams,
    viewFactory = viewFactory(null),
    plugins = listOf(interactor, router)
) {

    var isAttached: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()
        isAttached = true
    }

    fun getRouter() = router

    override fun onDestroy(isRecreating: Boolean) {
        super.onDestroy(isRecreating)
        isAttached = false
    }
}
