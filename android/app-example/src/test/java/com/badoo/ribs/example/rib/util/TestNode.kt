package com.badoo.ribs.example.rib.util

import android.view.ViewGroup
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.view.RibView
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock

class TestNode<V : RibView>(
    buildContext: BuildContext.Resolved<*> = mockBuildContext,
    router: Router<*, *, *, *, V> = mock(),
    viewFactory: ((ViewGroup) -> V?)? = mock(),
    interactor: Interactor<*, *, *, V> = mock()
) : Node<V>(
    buildContext = buildContext,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
) {

    var isAttached: Boolean = false
        private set

    override fun onAttach() {
        super.onAttach()
        isAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        isAttached = false
    }

    companion object {
        val mockBuildContext = mock<BuildContext.Resolved<Nothing?>> { on { identifier} doReturn mock() }
    }
}
