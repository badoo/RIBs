package com.badoo.ribs.example.rib.portal_overlay

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayView.Event
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PortalOverlayView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, PortalOverlayView>
}


class PortalOverlayViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : PortalOverlayView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_portal_overlay
    ) : PortalOverlayView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> PortalOverlayView = {
            PortalOverlayViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    override fun accept(vm: PortalOverlayView.ViewModel) {
    }
}
