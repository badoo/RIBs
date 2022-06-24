package com.badoo.ribs.sandbox.rib.portal_overlay_test

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTestView.Event
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTestView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PortalOverlayTestView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactoryBuilder<Nothing?, PortalOverlayTestView>
}


class PortalOverlayTestViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    PortalOverlayTestView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_portal_overlay_test
    ) : PortalOverlayTestView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<PortalOverlayTestView> = ViewFactory {
            PortalOverlayTestViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    override fun accept(vm: PortalOverlayTestView.ViewModel) {
    }
}
