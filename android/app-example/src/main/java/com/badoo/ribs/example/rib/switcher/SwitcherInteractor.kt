package com.badoo.ribs.example.rib.switcher

import android.arch.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.example.rib.switcher.SwitcherView.Event
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import io.reactivex.functions.Consumer

class SwitcherInteractor(
    router: Router<SwitcherRouter.Configuration, SwitcherView>,
    private val dialogToTestOverlay: DialogToTestOverlay
) : Interactor<SwitcherRouter.Configuration, SwitcherView>(
    router = router,
    disposables = null
) {
    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.createDestroy {
            bind(view to viewEventConsumer)
            bind(dialogToTestOverlay to dialogEventConsumer)
        }
    }

    internal inner class MenuListener : Consumer<Menu.Output> {
        override fun accept(output: Menu.Output) = when (output) {
            is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                Menu.MenuItem.FooBar -> {
                    router.push(Configuration.Foo)
                }
                Menu.MenuItem.HelloWorld -> {
                    router.push(Configuration.Hello)
                }
                Menu.MenuItem.Dialogs -> {
                    router.push(Configuration.DialogsExample)
                }
            }
        }
    }

    private val viewEventConsumer: Consumer<SwitcherView.Event> = Consumer {
        when (it) {
            Event.ShowOverlayDialogClicked -> router.push(Configuration.OverlayDialog)
            Event.ShowBlockerClicked -> router.push(Configuration.Blocker)
        }
    }

    internal val loremIpsumOutputConsumer: Consumer<Blocker.Output> = Consumer {
        // Clear Blocker
        router.popBackStack()
    }

    private val dialogEventConsumer : Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                router.popBackStack()
            }
        }
    }
}
