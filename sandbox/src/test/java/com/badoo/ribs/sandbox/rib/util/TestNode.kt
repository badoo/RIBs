package com.badoo.ribs.sandbox.rib.util

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.router.Router
import com.nhaarman.mockitokotlin2.mock

class TestNode<V : RibView>(
    buildParams: BuildParams<*> = BuildParams.Empty(),
    router: Router<*> = mock(),
    viewFactory: ((RibView) -> V?)? = mock(),
    interactor: Interactor<*, V> = mock()
) : Node<V>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = listOf(interactor, router)
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
}
