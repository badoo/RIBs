package com.badoo.ribs.core.plugin.utils.memoryleak

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeAwareImpl
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.badoo.ribs.core.view.RibView

class LeakDetector(
    private val watcher: (Any, String) -> Unit,
    private val nodeAware: NodeAware = NodeAwareImpl()
) : NodeAware by nodeAware,
    NodeLifecycleAware,
    ViewAware<RibView>,
    ViewLifecycleAware {

    private var view: RibView? = null

    override fun onViewCreated(view: RibView, viewLifecycle: Lifecycle) {
        this.view = view
        super.onViewCreated(view, viewLifecycle)
    }

    override fun onDetachFromView() {
        this.view?.let { watcher.invoke(it, "onDetachFromView") }
        this.view = null
        super.onDetachFromView()
    }

    override fun onDestroy() {
        watcher(node, "onDestroy")
        super.onDestroy()
    }
}
