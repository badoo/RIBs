package com.badoo.ribs.test.util.ribs

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory

class TestNode<V: RibView>(
    identifier: Rib,
    viewFactory: ViewFactory<V>,
    router: Router<*, V>,
    interactor: Interactor<*, V>
): Node<V>(
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

    override fun attachToView(parentViewGroup: ViewGroup) {
        super.attachToView(parentViewGroup)
    }

    override fun detachFromView() {
        super.detachFromView()
    }

    override fun onDetach() {
        super.onDetach()
        isAttached = false
    }
}
