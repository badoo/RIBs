package com.badoo.ribs.example.rib.portal_sub_screen

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter.Configuration
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter.Configuration.FullScreen
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenView.Event
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenView.ViewModel
import io.reactivex.functions.Consumer

class PortalSubScreenInteractor(
    portal: Portal.OtherSide,
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Nothing, PortalSubScreenView>
) : Interactor<Configuration, Content, Nothing, PortalSubScreenView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {
    override fun onViewCreated(view: PortalSubScreenView, viewLifecycle: Lifecycle) {
        view.accept(ViewModel("My id: " + id.replace("${PortalSubScreenInteractor::class.java.name}.", "")))
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.OpenBigClicked -> portal.showContent(router, FullScreen.ShowBig)
            Event.OpenOverlayClicked -> portal.showOverlay(router, FullScreen.ShowOverlay)
        }
    }
}
