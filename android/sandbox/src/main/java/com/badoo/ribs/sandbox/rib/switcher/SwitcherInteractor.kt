package com.badoo.ribs.sandbox.rib.switcher

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.configuration.feature.operation.pushOverlay
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.sandbox.rib.blocker.Blocker
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import io.reactivex.functions.Consumer

internal class SwitcherInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: SwitcherRouter,
    private val dialogToTestOverlay: DialogToTestOverlay
) : Interactor<Switcher, SwitcherView>(
    buildParams = buildParams,
    disposables = null
) {

    private val menuListener = Consumer<Menu.Output> { output ->
        when (output) {
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

    private val blockerOutputConsumer = Consumer<Blocker.Output> {
        // Clear Blocker
        router.popBackStack()
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.ShowOverlayDialogClicked -> router.pushOverlay(Overlay.Dialog)
            Event.ShowBlockerClicked -> router.push(Content.Blocker)
        }
    }

    private val dialogEventConsumer : Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                // do something if you want
            }
        }
    }

    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
            bind(dialogToTestOverlay to dialogEventConsumer)
        }
    }

    override fun onChildCreated(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is Menu -> {
                    bind(child.output to menuListener)
                    bind(router.menuUpdater to child.input)
                }
                is Blocker -> {
                    bind(child.output to blockerOutputConsumer)
                }
            }
        }
    }
}
