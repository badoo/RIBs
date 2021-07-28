package com.badoo.ribs.samples.gallery.rib.android_picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface AndroidPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object LaunchingActivitiesClicked : Event()
        object PermissionsClicked : Event()
        object DialogsClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, AndroidPickerView>
}


class AndroidPickerViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    AndroidPickerView,
    ObservableSource<Event> by events {

    class Factory : AndroidPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<AndroidPickerView> = ViewFactory {
            AndroidPickerViewImpl(
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
    ButtonList(
        listOf(
            "Launching Activities" to { events.accept(Event.LaunchingActivitiesClicked) },
            "Permissions" to { events.accept(Event.PermissionsClicked) },
            "Dialogs" to { events.accept(Event.DialogsClicked) },
        )
    )
}
