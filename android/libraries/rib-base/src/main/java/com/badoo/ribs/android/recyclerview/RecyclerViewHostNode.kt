package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

class RecyclerViewHostNode<T : Parcelable> internal constructor(
    buildParams: BuildParams<Nothing?>,
    plugins: List<Plugin>,
    private val viewDeps: RecyclerViewHostView.Dependency,
    private val timeCapsule: AndroidTimeCapsule,
    private val adapter: Adapter<T>
) : Node<RibView>(
    buildParams = buildParams,
    viewFactory = { RecyclerViewHostViewImpl.Factory().invoke(viewDeps).invoke(it) },
    plugins = plugins
), RecyclerViewHost<T> {

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    override fun onDetach() {
        adapter.onDestroy()
        super.onDetach()
    }
}
