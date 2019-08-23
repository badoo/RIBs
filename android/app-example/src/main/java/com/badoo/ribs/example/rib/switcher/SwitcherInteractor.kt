package com.badoo.ribs.example.rib.switcher

import android.arch.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.switcher.SwitcherView.Event
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import io.reactivex.functions.Consumer
import android.os.Bundle
import com.badoo.ribs.example.rib.switcher.feature.PortalFeature

class SwitcherInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Permanent, Content, Overlay, SwitcherView>,
    private val dialogToTestOverlay: DialogToTestOverlay
//    ,
//    private val portal: PortalFeature
) : Interactor<Configuration, Content, Overlay, SwitcherView>(
    savedInstanceState = savedInstanceState,
    router = router,
//    disposables = portal
    disposables = null
) {

    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.createDestroy {
            bind(view to viewEventConsumer)
            bind(dialogToTestOverlay to dialogEventConsumer)
//            bind(portal.models to view.portalRenderer)
//            bind(portal.news to portalNewsConsumer)
        }
    }

//    private val portalNewsConsumer: Consumer<PortalFeature.News> = Consumer {
//        when (it) {
//            is PortalFeature.News.Activated -> TODO() // bind lifecycle to Node
//            is PortalFeature.News.Deactivated -> TODO() // bind lifecycle to Node
//        }
//    }

    internal inner class MenuListener : Consumer<Menu.Output> {
        override fun accept(output: Menu.Output) = when (output) {
            is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                Menu.MenuItem.FooBar -> {
                    router.push(Content.Foo)
                }
                Menu.MenuItem.HelloWorld -> {
                    router.push(Content.Hello)
                }
                Menu.MenuItem.Dialogs -> {
                    router.push(Content.DialogsExample)
                }
            }
        }
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.ShowOverlayDialogClicked -> router.pushOverlay(Overlay.Dialog)
            Event.ShowBlockerClicked -> router.push(Content.Blocker)
        }
    }

    internal val loremIpsumOutputConsumer: Consumer<Blocker.Output> = Consumer {
        // Clear Blocker
        router.popBackStack()
    }

    private val dialogEventConsumer : Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                // do something if you want
            }
        }
    }
}
