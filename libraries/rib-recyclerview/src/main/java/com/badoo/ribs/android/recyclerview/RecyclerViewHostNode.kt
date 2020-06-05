package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView

internal class RecyclerViewHostNode<T : Parcelable>(
    buildParams: BuildParams<Nothing?>,
    plugins: List<Plugin>,
    private val viewDeps: RecyclerViewHostView.Dependency,
    private val timeCapsule: AndroidTimeCapsule,
    private val adapter: Adapter<T>,
    private val connector: NodeConnector<Input<T>, Nothing> = NodeConnector()
) : Node<RibView>(
    buildParams = buildParams,
    viewFactory = { RecyclerViewHostViewImpl.Factory().invoke(viewDeps).invoke(it) },
    plugins = plugins
), RecyclerViewHost<T>, Connectable<Input<T>, Nothing> by connector {

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    override fun onDetach() {
        adapter.onDestroy()
        super.onDetach()
    }
}
