package com.badoo.ribs.example.rib.small

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.small.SmallView.Event
import com.badoo.ribs.example.rib.small.SmallView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface SmallView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object OpenBigClicked : Event()
        object OpenOverlayClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, SmallView>
}


class SmallViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : SmallView,
    ObservableSource<Event> by events {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_small
    ) : SmallView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> SmallView = {
            SmallViewImpl(
                com.badoo.ribs.customisation.inflate(it, layoutRes)
            )
        }
    }

    private val idText = androidView.findViewById<TextView>(R.id.small_id)
    private val openBigButton = androidView.findViewById<Button>(R.id.open_big)
    private val openOverlayButton = androidView.findViewById<Button>(R.id.open_overlay)

    init {
        openBigButton.setOnClickListener { events.accept(Event.OpenBigClicked)  }
        openOverlayButton.setOnClickListener { events.accept(Event.OpenOverlayClicked)  }
    }

    override fun accept(vm: ViewModel) {
        idText.text = vm.text
    }
}
