package com.badoo.ribs.core.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.Plugin

abstract class AndroidRibView : RibView {

    fun <T : View> findViewById(@IdRes id: Int): T =
        androidView.findViewById(id)

    protected open fun getParentViewForChild(child: Node<*>): ViewGroup =
        androidView

    override fun attachChild(child: Node<*>, virtual: Node<*>) {
        val target = getParentViewForChild(virtual)
        child.onCreateView(this)
        child.view?.let { target.addView(it.androidView) }
        child.onAttachToView()
        child.plugins<AndroidViewLifecycleAware>().forEach {
            it.onAttachToView(target)
        }
    }

    override fun detachChild(child: Node<*>, virtual: Node<*>) {
        val target = getParentViewForChild(virtual)
        child.view?.let { target.removeView(it.androidView) }
        child.onDetachFromView()
        child.plugins<AndroidViewLifecycleAware>().forEach {
            it.onDetachFromView(target)
        }
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
