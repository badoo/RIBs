package com.badoo.ribs.example.rib.menu.builder

import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.core.Node

@MenuScope
@dagger.Component(
    modules = [MenuModule::class],
    dependencies = [
        Menu.Dependency::class,
        Menu.Customisation::class
    ]
)
interface MenuComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Menu.Dependency,
            customisation: Menu.Customisation
        ): MenuComponent
    }

    fun node(): Node<MenuView>
}
