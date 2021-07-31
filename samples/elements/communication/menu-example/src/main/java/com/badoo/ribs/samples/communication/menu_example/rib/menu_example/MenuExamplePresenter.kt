package com.badoo.ribs.samples.communication.menu_example.rib.menu_example

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.communication.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration
import com.badoo.ribs.samples.communication.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration.Content
import com.badoo.ribs.samples.communication.menu_example.rib.menu.Menu

interface MenuExamplePresenter

internal class MenuExamplePresenterImpl(
    private val backStack: BackStack<Configuration>
) : MenuExamplePresenter,
    SubtreeChangeAware {

    override fun onChildBuilt(child: Node<*>) {
        when (child) {
            is Menu -> child.output.observe(::onMenuOutput)
        }
    }

    private fun onMenuOutput(output: Menu.Output) {
        when (output) {
            is Menu.Output.MenuItemSelected -> updateContent(output.item)
        }
    }

    override fun onChildAttached(child: Node<*>) {
        when (child) {
            is Menu -> backStack.activeConfigurations.observe {
                updateMenuItem(it, child)
            }
        }
    }

    private fun updateContent(item: Menu.MenuItem) {
        backStack.push(item.toContent())
    }

    private fun updateMenuItem(content: Configuration, child: Menu) {
        content.toMenuItem()?.let { child.input.accept(Menu.Input.SelectMenuItem(it)) }
    }

    private fun Menu.MenuItem.toContent() =
        when (this) {
            Menu.MenuItem.Child1 -> Content.Child1
            Menu.MenuItem.Child2 -> Content.Child2
            Menu.MenuItem.Child3 -> Content.Child3
        }

    private fun Configuration.toMenuItem() =
        when (this) {
            is Content.Child1 -> Menu.MenuItem.Child1
            is Content.Child2 -> Menu.MenuItem.Child2
            is Content.Child3 -> Menu.MenuItem.Child3
            else -> null
        }
}
