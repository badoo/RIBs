package com.badoo.ribs.samples.gallery.rib.other.picker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.gallery.composable.ButtonList
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface OtherPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object RetainedInstanceStoreClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, OtherPickerView>
}


class OtherPickerViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    OtherPickerView,
    ObservableSource<Event> by events {

    class Factory : OtherPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<OtherPickerView> = ViewFactory {
            OtherPickerViewImpl(
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
            "RetainedInstanceStore" to { events.accept(Event.RetainedInstanceStoreClicked) },
        )
    )
}
