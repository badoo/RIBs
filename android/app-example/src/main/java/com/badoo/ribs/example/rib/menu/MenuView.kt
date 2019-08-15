package com.badoo.ribs.example.rib.menu

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.mvicore.modelWatcher
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.menu.Menu.MenuItem
import com.badoo.ribs.example.rib.menu.MenuView.Event
import com.badoo.ribs.example.rib.menu.MenuView.ViewModel
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
    private val allMenuItems = listOf(helloWorld, fooBar, dialogs)

    fun menuItem(id: Int, menuItem: MenuItem) : TextView =
        androidView.findViewById<TextView>(id).apply {
            setOnClickListener { events.accept(Event.Select(menuItem)) }
        }

    override fun accept(vm: ViewModel) {
        modelWatcher.invoke(vm)
    }

    private val modelWatcher = modelWatcher<ViewModel> {
        ViewModel::selected {
            allMenuItems.forEach { textView -> textView.resetHighlight() }
            it?.mapToTextView()?.highlight()
        }
    }

    fun TextView.resetHighlight() =
        setColor(R.color.material_grey_600)

    fun TextView.highlight() =
        setColor(R.color.material_blue_grey_950)

    fun TextView.setColor(i: Int) =
        setTextColor(ContextCompat.getColor(androidView.context, i))

    private fun MenuItem.mapToTextView() =
        when (this) {
            MenuItem.HelloWorld -> helloWorld
            MenuItem.FooBar -> fooBar
            MenuItem.Dialogs -> dialogs
        }
}
