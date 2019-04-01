package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration
import io.reactivex.functions.Consumer

class SwitcherInteractor(
    router: Router<Configuration, SwitcherView>
) : Interactor<Configuration, SwitcherView>(
    disposables = null,
    router = router
) {

    internal inner class MenuListener : Consumer<Menu.Output> {
        override fun accept(output: Menu.Output) = when (output) {
            is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                Menu.MenuItem.FooBar -> {
                    router.push(Configuration.Foo)
                }
                Menu.MenuItem.HelloWorld -> {
                    router.push(Configuration.Hello)
                }
            }
        }
    }

}
