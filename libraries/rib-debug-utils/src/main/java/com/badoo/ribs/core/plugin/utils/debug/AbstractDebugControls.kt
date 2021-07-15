package com.badoo.ribs.core.plugin.utils.debug

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.ribs.base.R
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeAwareImpl
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewLifecycleAware

@SuppressWarnings("LongParameterList")
abstract class AbstractDebugControls<T : Rib> internal constructor(
    private val viewFactory: ((ViewGroup) -> View)? = null,
    private var viewGroupForChildren: (() -> ViewGroup)? = null,
    private var growthDirection: GrowthDirection? = null,
    private val nodeAware: NodeAware = NodeAwareImpl(),
    private val ribAware: RibAware<T> = RibAwareImpl()
) : NodeAware by nodeAware,
    RibAware<T> by ribAware,
    ViewLifecycleAware {

    abstract val label: String
    private var container: ViewGroup? = null
    private var target: ViewGroup? = null
    protected var debugView: View? = null

    final override fun onAttachToView() {
        node.pluginUp<AbstractDebugControls<*>>()?.let { parent ->
            if (viewGroupForChildren == null) viewGroupForChildren = parent.viewGroupForChildren
            if (growthDirection == null) growthDirection = parent.growthDirection
        }

        target = viewGroupForChildren?.invoke()
        target?.let { target ->
            debugView = viewFactory?.invoke(target)
            debugView?.let { debugView ->
                container = target.inflate<ViewGroup>(R.layout.debug_controls_host).also {
                    it.findViewById<TextView>(R.id.label)?.text = label
                    it.findViewById<ViewGroup>(R.id.children_container).addView(debugView)
                }

                target.addView(container, if (growthDirection == GrowthDirection.TOP) 0 else -1)
                onDebugViewCreated(debugView)
            }
        }
    }

    final override fun onDetachFromView() {
        super.onDetachFromView()
        if (target != null) {
            debugView?.let {
                target?.removeView(container)
                onDebugViewDestroyed(it)
            }
            debugView = null
            target = null
        }
    }

    open fun onDebugViewCreated(debugView: View) {}

    open fun onDebugViewDestroyed(debugView: View) {}
}
