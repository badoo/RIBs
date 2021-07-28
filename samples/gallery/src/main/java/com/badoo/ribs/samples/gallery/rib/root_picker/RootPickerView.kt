package com.badoo.ribs.samples.gallery.rib.root_picker

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
        object RoutingClicked : Event()
        object CommunicationClicked : Event()
        object AndroidClicked : Event()
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
            onRoutingClicked = { events.accept(Event.RoutingClicked) },
            onCommunicationClicked = { events.accept(Event.CommunicationClicked) },
            onAndroidClicked = { events.accept(Event.AndroidClicked) },
        )
    }
}

@Preview
@Composable
private fun View(
    onRoutingClicked: () -> Unit = {},
    onCommunicationClicked: () -> Unit = {},
    onAndroidClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

        Column(modifier = Modifier.width(IntrinsicSize.Min)) {
            Button(
                onClick = onRoutingClicked,
                modifier = buttonModifier
            ) {
                Text("Routing")
            }
            Button(
                onClick = onCommunicationClicked,
                modifier = buttonModifier
            ) {
                Text("Communication")
            }
            Button(
                onClick = onAndroidClicked,
                modifier = buttonModifier
            ) {
                Text("Android")
            }
        }
    }
}
