package com.badoo.ribs.example.rib.util

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.nhaarman.mockitokotlin2.mock

class TestNode<V : RibView>(
    router: Router<*, *, *, *, V>,
    identifier: Rib = object : Rib {},
    viewFactory: ViewFactory<V> = mock(),
    interactor: Interactor<*, *, *, V> = mock()
) : Node<V>(
    identifier = identifier,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
) {

    var isAttached: Boolean = false
        private set

    override fun onAttach(savedInstanceState: Bundle?) {
        super.onAttach(savedInstanceState)
        isAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        isAttached = false
    }
}
