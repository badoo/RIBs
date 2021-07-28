package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface RoutingPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object SimpleRoutingClicked : Event()
        object BackStackClicked : Event()
        object TransitionsClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, RoutingPickerView>
}


class RoutingPickerViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    RoutingPickerView,
    ObservableSource<Event> by events {

    class Factory : RoutingPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<RoutingPickerView> = ViewFactory {
            RoutingPickerViewImpl(
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
    events: PublishRelay<Event> = PublishRelay.create(),
) {
    ButtonList(
        listOf(
            "Simple static routing" to { events.accept(Event.SimpleRoutingClicked) },
            "Back stack" to { events.accept(Event.BackStackClicked) },
            "Transition animations" to { events.accept(Event.TransitionsClicked) },
        )
    )
}
