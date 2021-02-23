package com.badoo.ribs.core.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.Plugin

abstract class AndroidRibView : RibView, LifecycleOwner {

    private lateinit var lifecycle: Lifecycle

    fun <T : View> findViewById(@IdRes id: Int): T =
        androidView.findViewById(id)

    protected open fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        androidView

    override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getParentViewForSubtree(subtreeOf)
        child.onCreateView(this)
        child.view?.let { target.addView(it.androidView) }
        child.onAttachToView()
        child.plugins<AndroidViewLifecycleAware>().forEach {
            it.onAttachToView(target)
        }
    }

    override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getParentViewForSubtree(subtreeOf)
        child.view?.let { target.removeView(it.androidView) }
        child.onDetachFromView()
        child.plugins<AndroidViewLifecycleAware>().forEach {
            it.onDetachFromView(target)
        }
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    @CallSuper
    override fun onCreate(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    /**
     * Instances of this plugin will not be notified by Node, but by [AndroidRibView] directly,
     * as the existence of a parentViewGroup depends on this view type (think Compose).
     *
     * This also means that any child implementing this interface cannot be guaranteed to be notified
     * at all, only if attached to a parent that has [AndroidRibView].
     */
    interface AndroidViewLifecycleAware : Plugin {
        fun onAttachToView(parentViewGroup: ViewGroup) {}
        fun onDetachFromView(parentViewGroup: ViewGroup) {}
    }
}
