package com.badoo.ribs.example.rib.portal_full_screen

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration.Overlay

class PortalFullScreenInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Overlay, PortalFullScreenView>
) : Interactor<Configuration, Content, Overlay, PortalFullScreenView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: PortalFullScreenView, viewLifecycle: Lifecycle) {
        view.accept(PortalFullScreenView.ViewModel("My id: " + id.replace("${PortalFullScreenInteractor::class.java.name}.", "")))
    }
}
