package com.badoo.ribs.sandbox.rib.compose_parent

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.compose.ComposeView
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentView.Event
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ComposeParentView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val someNumber: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, ComposeParentView>
}


class ComposeParentViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    ComposeParentView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory : ComposeParentView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ComposeParentView = {
            ComposeParentViewImpl(
                it.context
            )
        }
    }

    private var viewModel: MutableState<ViewModel> = mutableStateOf(ViewModel())
    private var content: MutableState<ComposeView?> = mutableStateOf(null)

    override val composable: @Composable () -> Unit = {
        Column {
            Text(text = "ComposeParentView")
            Text(text = "Own viewModel: ${viewModel.value}")
            Text(text = "Child:")
            Box(modifier = Modifier.padding(all = Dp(40f))) {
                content.value?.invoke()
            }
            Text(text = "Here be menu")
        }
    }

    override fun accept(vm: ViewModel) {
        viewModel.value = vm
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> =
        content
}
