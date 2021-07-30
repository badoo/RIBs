package com.badoo.ribs.samples.communication.menu_example.rib.menu

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.minimal.reactive.CompositeCancellable

interface MenuPresenter {
    fun onMenuItemSelected(item: Menu.MenuItem)
}

internal class MenuPresenterImpl(
    ribAware: RibAware<Menu> = RibAwareImpl()
) : MenuPresenter,
    ViewAware<MenuView>,
    RibAware<Menu> by ribAware {

    private var view: MenuView? = null
    private var cancellables = CompositeCancellable()

    override fun onViewCreated(view: MenuView, viewLifecycle: Lifecycle) {
        view.selectMenuItem(Menu.MenuItem.Child1)
        viewLifecycle.subscribe(
            onCreate = {
                this@MenuPresenterImpl.view = view
                cancellables += rib.input.observe(::onInputReceived)
            },
            onDestroy = {
                this@MenuPresenterImpl.view = null
                cancellables.cancel()
            }
        )
    }

    private fun onInputReceived(input: Menu.Input) {
        when (input) {
            is Menu.Input.SelectMenuItem -> view?.selectMenuItem(input.item)
        }
    }

    override fun onMenuItemSelected(item: Menu.MenuItem) {
        rib.output.accept(Menu.Output.MenuItemSelected(item))
    }
}
