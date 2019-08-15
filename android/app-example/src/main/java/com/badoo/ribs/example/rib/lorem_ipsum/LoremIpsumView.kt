package com.badoo.ribs.example.rib.lorem_ipsum

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import com.badoo.mvicore.modelWatcher
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView.Event
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface LoremIpsumView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    object ViewModel

    interface Factory : ViewFactory<Nothing?, LoremIpsumView>
}


class LoremIpsumViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : LoremIpsumView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_lorem_ipsum
    ) : LoremIpsumView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> LoremIpsumView = {
            LoremIpsumViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val button: Button = androidView.findViewById(R.id.lorem_ipsum_button)

    init {
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    private val modelWatcher = modelWatcher<ViewModel> {}

    override fun accept(vm: ViewModel) {
        modelWatcher.invoke(vm)
    }
}
