package com.badoo.ribs.example.rib.switcher

import android.arch.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.menu.Menu
import io.reactivex.Observable.interval
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

class SwitcherInteractor(
    router: Router<SwitcherRouter.Configuration, SwitcherView>
) : Interactor<SwitcherRouter.Configuration, SwitcherView>(
    router = router,
    disposables = null
) {
    @SuppressWarnings("MagicNumber", "OptionalUnit")
    private val blockerTicker = interval(10, TimeUnit.SECONDS)
        .map { Unit }
        .observeOn(AndroidSchedulers.mainThread())

    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.createDestroy {
            bind(blockerTicker to blockerEventConsumer)
        }
    }

    internal inner class MenuListener : Consumer<Menu.Output> {
        override fun accept(output: Menu.Output) = when (output) {
            is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                Menu.MenuItem.FooBar -> {
                    router.push(SwitcherRouter.Configuration.Foo)
                }
                Menu.MenuItem.HelloWorld -> {
                    router.push(SwitcherRouter.Configuration.Hello)
                }
                Menu.MenuItem.Dialogs -> {
                    router.push(SwitcherRouter.Configuration.DialogsExample)
                }
            }
        }
    }

    private val blockerEventConsumer: Consumer<Unit> = Consumer {
        router.push(SwitcherRouter.Configuration.Blocker)
    }

    internal val loremIpsumOutputConsumer: Consumer<Blocker.Output> = Consumer {
        // Clear Blocker
        router.popBackStack()
    }
}
