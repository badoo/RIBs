package com.badoo.ribs.sandbox.rib.menu.element

import com.badoo.ribs.sandbox.R

class MenuElement {
    val helloItem = MenuItemElement(R.id.menu_hello)
    val fooItem = MenuItemElement(R.id.menu_foo)
    val dialogsItem = MenuItemElement(R.id.menu_dialogs)

    private val all = listOf(helloItem, fooItem, dialogsItem)

    fun assertNothingSelected() = all.forEach { it.assertIsNotSelected() }
}
