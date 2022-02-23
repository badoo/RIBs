package com.badoo.ribs.sandbox.rib.compose_parent

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.compose.ComposeView
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentView.Event
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ComposeParentView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object NextClicked : Event()
    }

    data class ViewModel(
        val someNumber: Int = 0
    )

    interface Factory : ViewFactoryBuilder<Nothing?, ComposeParentView>
}


class ComposeParentViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    ComposeParentView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory : ComposeParentView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ComposeParentView> = ViewFactory {
            ComposeParentViewImpl(
                it.context
            )
        }
    }

    private var viewModel: MutableState<ViewModel> = mutableStateOf(ViewModel())
    private var content: MutableState<ComposeView?> = mutableStateOf(null)

    override val composable: @Composable () -> Unit = {
        View(viewModel.value, content.value, { events.accept(Event.NextClicked) })
    }

    override fun accept(vm: ViewModel) {
        viewModel.value = vm
    }

    override fun getParentStateForSubtree(subtreeOf: Node<*>): MutableState<ComposeView?> =
        content
}

@Composable
@SuppressWarnings("MagicNumber")
private fun View(viewModel: ViewModel, content: ComposeView?, onNextClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)

    ) {
        Column {
            Text(
                fontWeight = FontWeight.Bold,
                text = "ComposeParentView"
            )
            // Text(text = "Own viewModel: $viewModel")
            Text(
                text = "Change routing to next child:"
            )
            Button(
                modifier = Modifier.padding(top = Dp(16f)),
                onClick = onNextClicked
            ) {
                Text("Next")
            }
            Text(
                modifier = Modifier.padding(top = Dp(24f)),
                text = "Current child:"
            )
            Box(modifier = Modifier.padding(all = Dp(40f))) {
                content?.invoke()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewView() {
    View(
        viewModel = ViewModel(),
        content = null,
        onNextClicked = {}
    )
}
