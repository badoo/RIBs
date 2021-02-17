package com.badoo.ribs.samples.comms_nodes_1.rib.menu

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface MenuPresenter {
    fun onMenuItemSelected(item: Menu.MenuItem)
}

internal class MenuPresenterImpl(
    ribAware: RibAware<Menu> = RibAwareImpl()
) : MenuPresenter,
    ViewAware<MenuView>,
    RibAware<Menu> by ribAware {

    private var view: MenuView? = null
    private var disposable: Disposable? = null

    override fun onViewCreated(view: MenuView, viewLifecycle: Lifecycle) {
        view.selectMenuItem(Menu.MenuItem.Child1)
        viewLifecycle.subscribe(
            onCreate = {
                this@MenuPresenterImpl.view = view
                disposable = rib.input.subscribe(menuInputConsumer)
            },
            onDestroy = {
                this@MenuPresenterImpl.view = null
                disposable?.dispose()
                disposable = null
            }
        )
    }

    private val menuInputConsumer: Consumer<Menu.Input> = Consumer {
        when (it) {
            is Menu.Input.SelectMenuItem -> view?.selectMenuItem(it.item)
        }
    }

    override fun onMenuItemSelected(item: Menu.MenuItem) {
        rib.output.accept(Menu.Output.MenuItemSelected(item))
    }
}
