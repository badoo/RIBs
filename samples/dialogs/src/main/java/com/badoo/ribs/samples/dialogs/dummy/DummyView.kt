package com.badoo.ribs.samples.dialogs.dummy

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.dummy.LoremIpsumView.Event
import com.badoo.ribs.samples.dialogs.dummy.LoremIpsumView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface LoremIpsumView : RibView,
        ObservableSource<Event>,
        Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
            val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, LoremIpsumView>
}

class LoremIpsumViewImpl private constructor(
        override val androidView: ViewGroup,
        private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
        LoremIpsumView,
        ObservableSource<Event> by events,
        Consumer<ViewModel> {

    class Factory(
            @LayoutRes private val layoutRes: Int = R.layout.rib_dummy
    ) : LoremIpsumView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> LoremIpsumView = {
            LoremIpsumViewImpl(
                    it.inflate(layoutRes)
            )
        }
    }

    private val button: Button = androidView.findViewById(R.id.dummy_rib_button)

    init {
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
    }
}
