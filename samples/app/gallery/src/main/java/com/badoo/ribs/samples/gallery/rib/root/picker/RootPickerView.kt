package com.badoo.ribs.samples.gallery.rib.root.picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.root.picker.RootPickerView.Event
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource

interface RootPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        data object RoutingClicked : Event()
        data object CommunicationClicked : Event()
        data object AndroidClicked : Event()
        data object OtherClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, RootPickerView>
}


class RootPickerViewImpl private constructor(
    context: Context,
    lifecycle: Lifecycle,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context, lifecycle),
    RootPickerView,
    ObservableSource<Event> by events {

    class Factory : RootPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<RootPickerView> = ViewFactory {
            RootPickerViewImpl(
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
    events: PublishRelay<Event> = PublishRelay.create()
) {
    ButtonList(
        listOf(
            "Routing" to { events.accept(Event.RoutingClicked) },
            "Communication" to { events.accept(Event.CommunicationClicked) },
            "Android" to { events.accept(Event.AndroidClicked) },
            "Other" to { events.accept(Event.OtherClicked) },
        )
    )
}
