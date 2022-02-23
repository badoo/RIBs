package com.badoo.ribs.compose

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView


/**
 * RibView used for compose interoperability. It will wrap the view in a ComposeView.
 *
 * @param context Android context used to create the required views
 * @param childHostFactory Factory to provide ComposeChildHost, required to attach compose content
 * to a ViewGroup. Use a custom one if you need to customize the ViewGroup.
 */
abstract class ComposeRibView(
    override val context: Context,
    private val childHostFactory: (MutableState<ComposeView?>, context: Context) -> ComposeChildHost = { state, context ->
        ComposeChildHost(state, context)
    },
) : RibView {

    abstract val composable: ComposeView

    override val androidView: ViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        androidx.compose.ui.platform.ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent(composable)
        }
    }
    private val childHosts = mutableMapOf<MutableState<ComposeView?>, ComposeChildHost>()

    private fun getChildHostForSubtree(subtreeOf: Node<*>): ComposeChildHost {
        val state = getParentStateForSubtree(subtreeOf)
        return childHosts.getOrPut(
            state,
            { childHostFactory(state, context) }
        )
    }

    open fun getParentStateForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> {
        throw IllegalStateException("DefaultImplementation must not be invoked")
    }

    override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
        val childHost = getChildHostForSubtree(subtreeOf)
        child.onCreateView(this)?.let {
            childHost.addView(it.androidView)
        }
        child.onAttachToView()
    }

    override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getChildHostForSubtree(subtreeOf)
        child.onDetachFromView()?.let {
            target.removeView(it.androidView)
        }
    }
}
