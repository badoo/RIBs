package com.badoo.ribs.sandbox.rib.small

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration.FullScreen
import com.badoo.ribs.sandbox.rib.small.SmallView.Event
import com.badoo.ribs.sandbox.rib.small.SmallView.ViewModel
import io.reactivex.functions.Consumer

class SmallInteractor(
    private val buildParams: BuildParams<Nothing?>,
    private val backStack: BackStack<Configuration>,
    portal: Portal.OtherSide
) : Interactor<Small, SmallView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: SmallView, viewLifecycle: Lifecycle) {
        val uuid = buildParams.identifier.uuid.toString()
        view.accept(ViewModel("My id: " + uuid.replace("${SmallInteractor::class.java.name}.", "")))
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.OpenBigClicked -> portal.showContent(node, FullScreen.ShowBig)
            Event.OpenOverlayClicked -> portal.showOverlay(node, FullScreen.ShowOverlay)
        }
    }
}
