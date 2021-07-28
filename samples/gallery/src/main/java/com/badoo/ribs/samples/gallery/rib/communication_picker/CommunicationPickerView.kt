package com.badoo.ribs.samples.gallery.rib.communication_picker

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
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.gallery.rib.communication_picker.CommunicationPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface CommunicationPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object MenuExampleClicked : Event()
        object MultiScreenExampleClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, CommunicationPickerView>
}


class CommunicationPickerViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    CommunicationPickerView,
    ObservableSource<Event> by events {

    class Factory : CommunicationPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<CommunicationPickerView> = ViewFactory {
            CommunicationPickerViewImpl(
                it.parent.context
            )
        }
    }

    override val composable: @Composable () -> Unit = {
        View(events)
    }
}

@Preview
@Composable
private fun View(
    events: PublishRelay<Event> = PublishRelay.create()
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
                onClick = { events.accept(Event.MenuExampleClicked) },
                modifier = buttonModifier
            ) {
                Text("Menu example")
            }
            Button(
                onClick = { events.accept(Event.MultiScreenExampleClicked) },
                modifier = buttonModifier
            ) {
                Text("Multi-screen example")
            }
        }
    }
}
