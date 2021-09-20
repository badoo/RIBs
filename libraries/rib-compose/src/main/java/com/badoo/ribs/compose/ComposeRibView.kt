package com.badoo.ribs.compose

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView


/**
 * RibView used for compose interoperability. It will wrap the view in a ComposeView.
 *
 * @param context Android context used to create the required views
 * @param targetFactory Factory to provide ComposeTarget, required internally to attach compose content
 * to a ViewGroup. Use a custom one if you need to customize the ViewGroup.
 */
abstract class ComposeRibView(
    override val context: Context,
    private val targetFactory: (MutableState<ComposeView?>, context: Context) -> ComposeTarget = { state, context ->
        ComposeTarget(state, context)
    },
) : RibView {

    abstract val composable: ComposeView

    override val androidView: ViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        androidx.compose.ui.platform.ComposeView(context).apply {
            setContent(composable)
        }
    }
    private val targets = mutableMapOf<MutableState<ComposeView?>, ComposeTarget>()

    private fun getTargetForSubtree(subtreeOf: Node<*>): ComposeTarget {
        val state = getParentStateForSubtree(subtreeOf)
        return targets.getOrPut(
            state,
            { targetFactory(state, context) }
        )
    }

    open fun getParentStateForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> =
        mutableStateOf(null)

    override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getTargetForSubtree(subtreeOf)
        child.onCreateView(this)
        child.view?.let { target.addView(it.androidView) }
        child.onAttachToView()
    }


    override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getTargetForSubtree(subtreeOf)
        child.view?.let { target.removeView(it.androidView) }
        child.onDetachFromView()
    }
}
