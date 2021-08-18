package com.badoo.ribs.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.viewinterop.AndroidView
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

abstract class ComposeRibView(
    override val context: Context
) : RibView {

    abstract val composable: ComposeView

    /**
     * Only for compatibility with [com.badoo.ribs.core.view.AndroidRibView] parents.
     *
     * Will not be constructed / used if parent is also [ComposeRibView]. In those cases
     * current [composable] is used directly.
     */
    override val androidView: ViewGroup by lazy {
        androidx.compose.ui.platform.ComposeView(
            context = context,
            attrs = null,
            defStyleAttr = 0
        ).apply {
            setContent(composable)
        }
    }

    private var lastChildAttached: MutableMap<Any, Node<*>> = mutableMapOf()

    protected open fun getParentViewForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> =
        mutableStateOf(null)

    override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target: MutableState<ComposeView?> = getParentViewForSubtree(subtreeOf)
        lastChildAttached[target] = child

        when (val childView = child.onCreateView(this)) {
            is ComposeRibView -> {
                child.onAttachToView()
                target.value = childView.composable
            }

            else -> {
                val innerContainer = FrameLayout(context)
                AndroidRibViewHost(innerContainer).attachChild(child)
                target.value = { AndroidView(factory = { innerContainer }) }
            }
        }
    }

    override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
        child.onDetachFromView()
        val target: MutableState<ComposeView?> = getParentViewForSubtree(subtreeOf)

        // Only detach the same child, or we would remove something unintended.
        // If there was already another child attached to the same target, then the MutableState
        // of lastChildAttached[target] was overwritten, and the related ComposeView
        // was already removed from the composition as a result, and no further action is needed.
        if (child == lastChildAttached[target]) {
            target.value = null
            lastChildAttached.remove(target)
        }
    }
}
