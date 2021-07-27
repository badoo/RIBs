package com.badoo.ribs.samples.gallery.rib.root_picker

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.rib.root_picker.RootPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface RootPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object Item1Clicked : Event()
        object Item2Clicked : Event()
        object Item3Clicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, RootPickerView>
}


class RootPickerViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    RootPickerView,
    ObservableSource<Event> by events {

    class Factory : RootPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<RootPickerView> = ViewFactory {
            RootPickerViewImpl(
                it.parent.context
            )
        }
    }

    override val composable: @Composable () -> Unit = {
        View(
            onItem1Clicked = { events.accept(Event.Item1Clicked) },
            onItem2Clicked = { events.accept(Event.Item2Clicked) },
            onItem3Clicked = { events.accept(Event.Item3Clicked) },
        )
    }
}

@Preview
@Composable
private fun View(
    onItem1Clicked: () -> Unit = {},
    onItem2Clicked: () -> Unit = {},
    onItem3Clicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center
    ) {
        val padding = Modifier.padding(8.dp)

        Column {
            Button(
                onClick = { },
                modifier = padding
            ) {
                Text("Item 1")
            }
            Button(
                onClick = { },
                modifier = padding
            ) {
                Text("Item 2")
            }
            Button(
                onClick = { },
                modifier = padding
            ) {
                Text("Item 2")
            }
        }
    }
}
