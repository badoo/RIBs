package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

class RecyclerViewHostNode<T : Parcelable> internal constructor(
    savedInstanceState: Bundle? = null,
    router: RecyclerViewHostRouter<T>,
    private val viewDeps: RecyclerViewHostView.Dependency,
    interactor: RecyclerViewHostInteractor<T>,
    private val timeCapsule: AndroidTimeCapsule
) : Node<RibView>(
    identifier = object : RecyclerViewHost {},
    savedInstanceState = savedInstanceState,
    viewFactory = { RecyclerViewHostViewImpl.Factory().invoke(viewDeps).invoke(it) },
    router = router,
    interactor = interactor
) {
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }
}
