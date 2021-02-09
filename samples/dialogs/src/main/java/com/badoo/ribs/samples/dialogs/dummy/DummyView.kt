package com.badoo.ribs.samples.dialogs.dummy

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.dummy.DummyView.Event
import com.badoo.ribs.samples.dialogs.dummy.DummyView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface DummyView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, DummyView>
}

class DummyViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    DummyView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dummy
    ) : DummyView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> DummyView = {
            DummyViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val button: Button = androidView.findViewById(R.id.dummy_rib_button)

    init {
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    @SuppressWarnings("EmptyFunctionBlock")
    override fun accept(vm: ViewModel) {
    }
}
