package com.badoo.ribs.example.rib.root

import android.os.Bundle
import com.badoo.ribs.core.Interactor
//import com.badoo.ribs.core.routing.portal.Portal.Source.Model.*
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.root.RootRouter.Configuration
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Content

class RootInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Nothing, Nothing>
//    private val portal: PortalFeature
) : Interactor<Configuration, Content, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
//), Portal.Sink {
) {

//    override fun accept(command: Portal.Sink.Command) {
//        when (command) {
//            is Portal.Sink.Command.Add -> router.push(Content.Portal(command.node))
//            is Portal.Sink.Command.Remove -> router.popBackStack()
//        }
//    }

//    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
//        ribLifecycle.createDestroy {
//            bind(portal.models to portalModelConsumer)
//        }
//    }

//    override fun onViewCreated(view: RootView, viewLifecycle: Lifecycle) {
//        viewLifecycle.createDestroy {
////            bind(portal.models to view.portalRenderer)
//            bind(portal.models to view)
//        }
//    }

//    private val portalModelConsumer: Consumer<Portal.Source.Model> = Consumer {
//        when (it) {
//            is Empty -> router.newRoot(Content.Default) // TODO pop should work just fine, but this is safer for now
//            is Showing -> router.push(Content.Portal)
//        }
//    }
}
