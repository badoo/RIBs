package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.menu.Menu
import io.reactivex.functions.Consumer

class SwitcherInteractor(
    private val router: Router<SwitcherRouter.Configuration, SwitcherView>
) : Interactor<SwitcherView>(
    disposables = null
) {

    internal inner class MenuListener : Consumer<Menu.Output> {
        override fun accept(output: Menu.Output) = when (output) {
            is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                Menu.MenuItem.FooBar -> {
                    router.push(SwitcherRouter.Configuration.Foo)
                }
                Menu.MenuItem.HelloWorld -> {
                    router.push(SwitcherRouter.Configuration.Hello)
                }
            }
        }
    }

}
