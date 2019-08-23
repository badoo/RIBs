package com.badoo.ribs.example.rib.root

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Portal.Source.Model.*
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.root.RootRouter.Configuration
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Content
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.switcher.feature.PortalFeature
import io.reactivex.functions.Consumer

class RootInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Overlay, RootView>,
    private val portal: PortalFeature
) : Interactor<Configuration, Content, Overlay, RootView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = portal
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(portal.models to portalModelConsumer)
        }
    }

    override fun onViewCreated(view: RootView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
//            bind(portal.models to view.portalRenderer)
            bind(portal.models to view)
        }
    }

    private val portalModelConsumer: Consumer<Portal.Source.Model> = Consumer {
        when (it) {
            is Empty -> router.newRoot(Content.Default) // TODO pop should work just fine, but this is safer for now
            is Showing -> router.push(Content.Portal)
        }
    }
}
