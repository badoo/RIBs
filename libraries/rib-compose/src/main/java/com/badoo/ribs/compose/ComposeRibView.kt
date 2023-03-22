package com.badoo.ribs.compose

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView2
import androidx.compose.ui.platform.ComposeView as ComposeAndroidView


/**
 * RibView used for compose interoperability. It will wrap the view in a ComposeView.
 *
 * @param context Android context used to create the required views
 * @param childHostFactory Factory to provide ComposeChildHost, required to attach compose content
 * to a ViewGroup. Use a custom one if you need to customize the ViewGroup.
 */
@Stable
abstract class ComposeRibView(
    override val context: Context,
    lifecycle: Lifecycle,
    private val childHostFactory: (MutableState<ComposeView?>, context: Context) -> ComposeChildHost = { state, context ->
        ComposeChildHost(state, context)
    },
) : AndroidRibView2(
    androidView = ComposeAndroidView(context),
    lifecycleDelegate = lifecycle,
) {

    abstract val composable: ComposeView

    init {
        val composeView = androidView as ComposeAndroidView
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycle)
        )
        lifecycle.subscribe(onCreate = { composeView.setContent(composable) })
    }

    private val childHosts = mutableMapOf<MutableState<ComposeView?>, ComposeChildHost>()

    private fun getChildHostForSubtree(subtreeOf: Node<*>): ComposeChildHost {
        val state = getParentStateForSubtree(subtreeOf)
        return childHosts.getOrPut(state) { childHostFactory(state, context) }
    }

    open fun getParentStateForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> {
        throw IllegalStateException("DefaultImplementation must not be invoked")
    }

    override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
        val childHost = getChildHostForSubtree(subtreeOf)
        child.onCreateView(this)?.also {
            childHost.addView(it.androidView)
        }
        child.onAttachToView()
    }

    override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
        val target = getChildHostForSubtree(subtreeOf)
        child.onDetachFromView()?.also {
            target.removeView(it.androidView)
        }
    }
}
