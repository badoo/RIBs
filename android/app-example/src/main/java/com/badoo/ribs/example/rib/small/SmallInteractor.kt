package com.badoo.ribs.example.rib.small

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.Content
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.FullScreen
import com.badoo.ribs.example.rib.small.SmallView.Event
import com.badoo.ribs.example.rib.small.SmallView.ViewModel
import io.reactivex.functions.Consumer

class SmallInteractor(
    portal: Portal,
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Nothing, SmallView>
    ) : Interactor<Configuration, Content, Nothing, SmallView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {
    override fun onViewCreated(view: SmallView, viewLifecycle: Lifecycle) {
        view.accept(ViewModel("My id: " + id.replace("${SmallInteractor::class.java.name}.", "")))
        viewLifecycle.createDestroy {
            bind(view to viewEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.OpenBigClicked -> portal.push(router.node.resolverChain() + FullScreen.ShowBig)
        }
    }
}
