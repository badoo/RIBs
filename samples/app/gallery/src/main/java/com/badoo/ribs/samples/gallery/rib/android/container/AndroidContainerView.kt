package com.badoo.ribs.samples.gallery.rib.android.container

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.compose.ComposeView
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder

interface AndroidContainerView : RibView {

    fun interface Factory : ViewFactoryBuilder<Nothing?, AndroidContainerView>
}


class AndroidContainerViewImpl private constructor(
    context: Context
) : ComposeRibView(context),
    AndroidContainerView {

    class Factory : AndroidContainerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<AndroidContainerView> = ViewFactory {
            AndroidContainerViewImpl(
                it.context
            )
        }
    }

    private var content: MutableState<ComposeView?> = mutableStateOf(null)

    override fun getParentStateForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> =
        content

    override val composable: @Composable () -> Unit = {
        View(content.value)
    }
}

@Preview
@Composable
private fun View(
    child: ComposeView? = null
) {
    child?.invoke()
}
