package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.core.builder.BuildParams
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.configuration.feature.operation.pushOverlay
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.sandbox.rib.blocker.Blocker
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.switcher.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import io.reactivex.functions.Consumer

class SwitcherInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: Router<Configuration, Permanent, Content, Overlay, SwitcherView>,
    private val dialogToTestOverlay: DialogToTestOverlay
) : Interactor<SwitcherView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
            bind(dialogToTestOverlay to dialogEventConsumer)
        }
    }

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
