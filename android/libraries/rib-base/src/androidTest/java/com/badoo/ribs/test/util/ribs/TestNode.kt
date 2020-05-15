package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory

class TestNode<V: RibView>(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<Nothing?, V>,
    private val router: Router<*, *, *, *, V>,
    interactor: Interactor<V>
): Node<V>(
    buildParams = buildParams,
    viewFactory = viewFactory(null),
    plugins = listOf(interactor, router)
) {

    var isAttached: Boolean = false
        private set

    override fun onAttach() {
        super.onAttach()
        isAttached = true
    }

    fun getRouter() = router

    override fun onDetach() {
        super.onDetach()
        isAttached = false
    }
}
