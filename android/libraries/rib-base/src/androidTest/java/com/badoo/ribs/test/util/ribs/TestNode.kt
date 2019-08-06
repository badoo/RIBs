package com.badoo.ribs.test.util.ribs

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory

class TestNode<V: RibView>(
    savedInstanceState: Bundle?,
    identifier: Rib,
    viewFactory: ViewFactory<Nothing?, V>,
    private val router: Router<*, *, *, *, V>,
    interactor: Interactor<*, *, *, V>
): Node<V>(
    savedInstanceState = savedInstanceState,
    identifier = identifier,
    viewFactory = viewFactory(null),
    router = router,
    interactor = interactor
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
