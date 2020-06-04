package com.badoo.ribs.sandbox.rib.switcher

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.core.routing.source.backstack.operation.pop
import com.badoo.ribs.core.routing.source.backstack.operation.push
import com.badoo.ribs.core.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.sandbox.rib.blocker.Blocker
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.Dialogs
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import io.reactivex.functions.Consumer

internal class SwitcherInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStackFeature<Configuration>,
    private val dialogToTestOverlay: DialogToTestOverlay
) : Interactor<Switcher, SwitcherView>(
    buildParams = buildParams,
    disposables = null
) {

    private val menuListener = Consumer<Menu.Output> { output ->
            when (output) {
              is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                FooBar -> {
                    backStack.push(Content.Foo)
                }
                HelloWorld -> {
                    backStack.push(Content.Hello)
                }
                Dialogs -> {
                    backStack.push(Content.DialogsExample)
                }
            }
        }
    }

    private val blockerOutputConsumer = Consumer<Blocker.Output> {
        // Clear Blocker
        // FIXME with remove
        backStack.popBackStack()
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.ShowOverlayDialogClicked -> backStack.pushOverlay(Overlay.Dialog)
            Event.ShowBlockerClicked -> backStack.push(Content.Blocker)
        }
    }

    internal val loremIpsumOutputConsumer: Consumer<Blocker.Output> = Consumer {
        // Clear Blocker
        backStack.pop()
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
                    bind(backStack.activeConfiguration to child.input using ConfigurationToMenuInput)
                }
                is Blocker -> {
                    bind(child.output to blockerOutputConsumer)
                }
            }
        }
    }

    object ConfigurationToMenuInput : (Configuration) -> Menu.Input? {
        override fun invoke(configuration: Configuration): Menu.Input? =
            when (configuration) {
                Content.Hello -> SelectMenuItem(HelloWorld)
                Content.Foo -> SelectMenuItem(FooBar)
                Content.DialogsExample -> SelectMenuItem(Dialogs)
                else -> null
            }
    }
}
