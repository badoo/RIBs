package com.badoo.ribs.example.rib.blocker

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.blocker.BlockerView.Event
import com.badoo.ribs.example.rib.blocker.BlockerView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface BlockerView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, BlockerView>
}

class BlockerViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : BlockerView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_blocker
    ) : BlockerView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> BlockerView = {
            BlockerViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val button: Button = androidView.findViewById(R.id.blocker_button)

    init {
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
    }
}
