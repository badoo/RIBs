package com.badoo.ribs.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.MutableState
import androidx.compose.Recomposer
import androidx.compose.mutableStateOf
import androidx.ui.core.setContent
import androidx.ui.viewinterop.AndroidView
import com.badoo.ribs.android.attach
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

abstract class ComposeRibView(
    val context: Context
) : RibView {

    abstract val composable: ComposeView

    /**
     * Only for compatibility with [AndroidRibView] parents.
     *
     * Will not be constructed / used if parent is also [ComposeRibView]. In those cases
     * current [composable] is used directly.
     */
    override val androidView: ViewGroup by lazy {
        FrameLayout(context).apply {
            setContent(Recomposer.current()) {
                composable()
            }
        }
    }

    protected open fun targetForChild(child: Node<*>): MutableState<ComposeView?> =
        mutableStateOf(null)

    override fun attachChild(child: Node<*>) {
        val target = targetForChild(child)

        when (val childView = child.onCreateView(androidView)) {
            is ComposeRibView ->{
                child.onAttachToView()
                target.value = childView.composable
            }

            else -> {
                val innerContainer = FrameLayout(context)
                innerContainer.attach(child)
                target.value = { AndroidView(innerContainer) }
            }
        }
    }

    override fun detachChild(child: Node<*>) {
        child.onDetachFromView()
        targetForChild(child).value = null
    }
}
