package com.badoo.ribs.sandbox.rib.compose_leaf

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.Event
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.ViewModel
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer

interface ComposeLeafView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactoryBuilder<Nothing?, ComposeLeafView>
}


class ComposeLeafViewImpl private constructor(
    context: Context,
    lifecycle: Lifecycle,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context, lifecycle),
    ComposeLeafView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory : ComposeLeafView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ComposeLeafView> = ViewFactory {
            ComposeLeafViewImpl(
                it.parent.context,
                it.lifecycle,
            )
        }
    }

    private var viewModel: MutableState<ViewModel> = mutableStateOf(ViewModel())

    override fun accept(vm: ViewModel) {
        viewModel.value = vm
    }

    override val composable: @Composable () -> Unit = {
        View(viewModel.value)
    }
}


@Composable
@SuppressWarnings("MagicNumber")
private fun View(viewModel: ViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = when (viewModel.i % 3) {
                    0 -> Color(0xFF009ABF)
                    1 -> Color(0xFFFFD08A)
                    2 -> Color(0xFFFF6C37)
                    else -> Color.LightGray
                }
            ),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = "ComposeLeafView"
            )
            Text(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                text = viewModel.i.toString()
            )
            LocalState()
        }
    }
}

@Composable
private fun LocalState(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        var counter by rememberSaveable { mutableIntStateOf(0) }
        Text(text = "LocalState: $counter")
        Button(onClick = { counter++ }) {
            Text(text = "Add")
        }
    }
}

@Preview
@Composable
private fun PreviewView() {
    View(
        viewModel = ViewModel(1)
    )
}
