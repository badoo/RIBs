package com.badoo.ribs.samples.gallery.rib.android.picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPickerView.Event
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource

interface AndroidPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        data object LaunchingActivitiesClicked : Event()
        data object PermissionsClicked : Event()
        data object DialogsClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, AndroidPickerView>
}


class AndroidPickerViewImpl private constructor(
    context: Context,
    lifecycle: Lifecycle,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context, lifecycle),
    AndroidPickerView,
    ObservableSource<Event> by events {

    class Factory : AndroidPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<AndroidPickerView> = ViewFactory {
            AndroidPickerViewImpl(
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
            "Launching Activities" to { events.accept(Event.LaunchingActivitiesClicked) },
            "Permissions" to { events.accept(Event.PermissionsClicked) },
            "Dialogs" to { events.accept(Event.DialogsClicked) },
        )
    )
}
