package com.badoo.ribs.sandbox.rib.blocker

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.blocker.BlockerView.Event
import com.badoo.ribs.sandbox.rib.blocker.BlockerView.ViewModel
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

    interface Factory : ViewFactoryBuilder<Nothing?, BlockerView>
}

class BlockerViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    BlockerView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_blocker
    ) : BlockerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<BlockerView> = ViewFactory {
            BlockerViewImpl(
                it.inflate(layoutRes)
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
