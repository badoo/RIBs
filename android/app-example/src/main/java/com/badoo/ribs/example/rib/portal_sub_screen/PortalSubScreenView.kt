package com.badoo.ribs.example.rib.portal_sub_screen

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenView.Event
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PortalSubScreenView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object OpenBigClicked : Event()
        object OpenOverlayClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, PortalSubScreenView>
}


class PortalSubScreenViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : PortalSubScreenView,
    ObservableSource<Event> by events {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_portal_sub_screen
    ) : PortalSubScreenView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> PortalSubScreenView = {
            PortalSubScreenViewImpl(
                inflate(it, layoutRes)
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
