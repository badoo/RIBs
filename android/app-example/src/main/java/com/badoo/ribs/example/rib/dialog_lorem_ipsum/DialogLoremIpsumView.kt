package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumView.Event
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface DialogLoremIpsumView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, DialogLoremIpsumView>
}


class DialogLoremIpsumViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : DialogLoremIpsumView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dialog_lorem_ipsum
    ) : DialogLoremIpsumView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> DialogLoremIpsumView = {
            DialogLoremIpsumViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val button: Button = androidView.findViewById(R.id.lorem_ipsum_button)

    init {
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
    }
}
