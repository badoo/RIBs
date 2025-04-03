package com.badoo.ribs.samples.gallery.rib.routing.routing_picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPickerView.Event
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource

interface RoutingPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        data object SimpleRoutingClicked : Event()
        data object BackStackClicked : Event()
        data object ParameterisedRoutingClicked : Event()
        data object TransitionsClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, RoutingPickerView>
}


class RoutingPickerViewImpl private constructor(
    context: Context,
    lifecycle: Lifecycle,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context, lifecycle),
    RoutingPickerView,
    ObservableSource<Event> by events {

    class Factory : RoutingPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<RoutingPickerView> = ViewFactory {
            RoutingPickerViewImpl(
                it.parent.context,
                it.lifecycle,
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
            "Parameterised routing" to { events.accept(Event.ParameterisedRoutingClicked) },
            "Transition animations" to { events.accept(Event.TransitionsClicked) },
        )
    )
}
