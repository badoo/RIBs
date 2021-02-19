package com.badoo.ribs.sandbox.rib.switcher

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.state.rx2
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.router.Router.TransitionState.IN_TRANSITION
import com.badoo.ribs.routing.router.Router.TransitionState.SETTLED
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.pop
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.sandbox.rib.blocker.Blocker
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.Compose
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.Dialogs
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.ViewModel
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@SuppressWarnings("LongParameterList")
internal class SwitcherInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
    private val dialogToTestOverlay: DialogToTestOverlay,
    private val transitions: ObservableSource<Router.TransitionState>,
    private val transitionSettled: () -> Boolean
) : Interactor<Switcher, SwitcherView>(
    buildParams = buildParams
) {
    private val menuListener = Consumer<Menu.Output> { output ->
        when (output) {
            is Menu.Output.MenuItemSelected -> {
                if (transitionSettled()) {
                    backStack.push(
                        when (output.menuItem) {
                            FooBar -> Content.Foo
                            HelloWorld -> Content.Hello
                            Dialogs -> Content.DialogsExample
                            Compose -> Content.Compose
                        }
                    )
                } else {
                    Log.d("SwitcherInteractor", "Menu output suppressed by running transition")
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

    private val transitionStateToViewModel : (Router.TransitionState) -> ViewModel = {
        when (it) {
            SETTLED -> ViewModel(uiFrozen = false)
            IN_TRANSITION -> ViewModel(uiFrozen = true)
        }
    }

    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
            bind(dialogToTestOverlay to dialogEventConsumer)
            bind(transitions to view using transitionStateToViewModel)
        }
    }

    override fun onChildBuilt(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is Menu -> bind(child.output to menuListener)
                is Blocker -> bind(child.output to blockerOutputConsumer)
            }
        }
    }

    override fun onChildAttached(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is Menu -> bind(backStack.activeConfigurations.rx2() to child.input using ConfigurationToMenuInput)
            }
        }
    }

    object ConfigurationToMenuInput : (Configuration) -> Menu.Input? {
        override fun invoke(configuration: Configuration): Menu.Input? =
            when (configuration) {
                Content.Hello -> SelectMenuItem(HelloWorld)
                Content.Foo -> SelectMenuItem(FooBar)
                Content.DialogsExample -> SelectMenuItem(Dialogs)
                Content.Compose -> SelectMenuItem(Compose)
                else -> null
            }
    }
}
