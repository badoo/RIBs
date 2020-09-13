package com.badoo.ribs.sandbox.rib.compose_parent

import android.content.Context
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.mutableStateOf
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.unit.Dp
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
    private val events: PublishRelay<ComposeParentView.Event> = PublishRelay.create()
) : ComposeRibView(context),
    ComposeParentView,
    ObservableSource<ComposeParentView.Event> by events,
    Consumer<ViewModel> {

    class Factory() : ComposeParentView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ComposeParentView = {
            ComposeParentViewImpl(
                it.context
            )
        }
    }

    private var viewModel: MutableState<ViewModel> = mutableStateOf(ViewModel())
    private var content: MutableState<ComposeView?> = mutableStateOf(null)

    override val composable: @Composable() () -> Unit = {
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

    override fun targetForChild(child: Node<*>): MutableState<ComposeView?> =
        content
}
