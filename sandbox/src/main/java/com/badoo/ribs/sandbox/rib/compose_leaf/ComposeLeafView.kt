package com.badoo.ribs.sandbox.rib.compose_leaf

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.Event
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

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
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    ComposeLeafView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory : ComposeLeafView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ComposeLeafView> = ViewFactory {
            ComposeLeafViewImpl(
                it.context
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
