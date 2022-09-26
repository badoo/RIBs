package com.badoo.ribs.core.view

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.Plugin

@Deprecated(
    message = "AndroidRibView does not support AndroidX integrations, " +
            "use AndroidRibView2 instead (backward incompatible change).",
    replaceWith = ReplaceWith("AndroidRibView2", "com.badoo.ribs.core.view.AndroidRibView2"),
)
abstract class AndroidRibView : RibView {

    fun <T : View> findViewById(@IdRes id: Int): T =
        androidView.findViewById(id)

    protected open fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        androidView

    override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getParentViewForSubtree(subtreeOf)
        child.onCreateView(this)?.also {
            target.addView(it.androidView)
        }
        child.onAttachToView()
        child.plugins<AndroidViewLifecycleAware>().forEach {
            it.onAttachToView(target)
        }
    }

    override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getParentViewForSubtree(subtreeOf)
        child.onDetachFromView()?.also {
            target.removeView(it.androidView)
        }
        child.plugins<AndroidViewLifecycleAware>().forEach {
            it.onDetachFromView(target)
        }
    }

    @CallSuper
    override fun saveInstanceState(bundle: Bundle) {
        val savedViewState = SparseArray<Parcelable>()
        androidView.saveHierarchyState(savedViewState)
        bundle.putSparseParcelableArray(KEY_VIEW_STATE, savedViewState)
    }

    @CallSuper
    override fun restoreInstanceState(bundle: Bundle?) {
        val savedViewState = bundle?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE)
        if (savedViewState != null) {
            androidView.restoreHierarchyState(savedViewState)
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

    companion object {
        const val KEY_VIEW_STATE = "view.hierarchy_state"
    }

}
