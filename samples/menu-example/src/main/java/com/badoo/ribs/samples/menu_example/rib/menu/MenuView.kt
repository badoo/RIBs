package com.badoo.ribs.samples.menu_example.rib.menu

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.menu_example.R

interface MenuView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, MenuView>

    interface Dependency {
        val presenter: MenuPresenter
    }

    fun selectMenuItem(item: Menu.MenuItem)
}

class MenuViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: MenuPresenter
) : AndroidRibView(),
    MenuView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_menu
    ) : MenuView.Factory {
        override fun invoke(deps: MenuView.Dependency): ViewFactory<MenuView> = ViewFactory {
            MenuViewImpl(
                it.inflate(layoutRes),
                presenter = deps.presenter
            )
        }
    }

    private val child1: TextView = androidView.findViewById(R.id.child1)
    private val child2: TextView = androidView.findViewById(R.id.child2)
    private val child3: TextView = androidView.findViewById(R.id.child3)

    init {
        child1.setOnClickListener { presenter.onMenuItemSelected(Menu.MenuItem.Child1) }
        child2.setOnClickListener { presenter.onMenuItemSelected(Menu.MenuItem.Child2) }
        child3.setOnClickListener { presenter.onMenuItemSelected(Menu.MenuItem.Child3) }
    }

    override fun selectMenuItem(item: Menu.MenuItem) {
        listOf(child1, child2, child3).forEach {
            it.typeface = Typeface.SANS_SERIF
            it.setTextColor(ContextCompat.getColor(androidView.context, R.color.text_default))
        }

        when(item) {
            Menu.MenuItem.Child1 -> child1
            Menu.MenuItem.Child2 -> child2
            Menu.MenuItem.Child3 -> child3
        }.apply {
            setTypeface(null, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(androidView.context, R.color.text_selected))
        }
    }
}
