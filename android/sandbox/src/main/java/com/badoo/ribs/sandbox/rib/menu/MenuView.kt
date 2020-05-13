package com.badoo.ribs.sandbox.rib.menu

import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem
import com.badoo.ribs.sandbox.rib.menu.MenuView.Event
import com.badoo.ribs.sandbox.rib.menu.MenuView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface MenuView : RibView, ObservableSource<Event>, Consumer<ViewModel> {

    sealed class Event {
        data class Select(val menuItem: MenuItem) : Event()
    }

    data class ViewModel(
        val selected: MenuItem?
    )

    interface Factory : ViewFactory<Nothing?, MenuView>
}


class MenuViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : MenuView,
    ObservableSource<Event> by events {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_menu
    ) : MenuView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> MenuView = {
            MenuViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private var helloWorld: TextView = menuItem(R.id.menu_hello, MenuItem.HelloWorld)
    private var fooBar: TextView = menuItem(R.id.menu_foo, MenuItem.FooBar)
    private var dialogs: TextView = menuItem(R.id.menu_dialogs, MenuItem.Dialogs)

    fun menuItem(id: Int, menuItem: MenuItem) : TextView =
        androidView.findViewById<TextView>(id).apply {
            setOnClickListener { events.accept(Event.Select(menuItem)) }
        }

    override fun accept(vm: ViewModel) {
        listOf(helloWorld, fooBar, dialogs).forEach {
            it.setTextColor(ContextCompat.getColor(androidView.context, R.color.material_grey_600))
        }

        vm.selected?.let {
            when (it) {
                MenuItem.HelloWorld -> helloWorld
                MenuItem.FooBar -> fooBar
                MenuItem.Dialogs -> dialogs
            }.apply {
                setTextColor(ContextCompat.getColor(context, R.color.material_blue_grey_950))
            }
        }
    }
}
