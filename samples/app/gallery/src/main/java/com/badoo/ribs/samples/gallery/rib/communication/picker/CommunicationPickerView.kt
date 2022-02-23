package com.badoo.ribs.samples.gallery.rib.communication.picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface CommunicationPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object MenuExampleClicked : Event()
        object CoordinateMultipleExampleClicked : Event()
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
                it.context
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
    ButtonList(
        listOf(
            "Menu example" to { events.accept(Event.MenuExampleClicked) },
            "Coordinate between multiple RIBs" to { events.accept(Event.CoordinateMultipleExampleClicked) },
        )
    )
}
