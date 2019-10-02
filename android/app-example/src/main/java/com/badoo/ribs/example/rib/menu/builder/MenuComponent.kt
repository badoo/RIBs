package com.badoo.ribs.example.rib.menu.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.core.Node
import dagger.BindsInstance

@MenuScope
@dagger.Component(
    modules = [MenuModule::class],
    dependencies = [Menu.Dependency::class]
)
interface MenuComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Menu.Dependency,
            @BindsInstance customisation: Menu.Customisation,
            @BindsInstance buildContext: BuildContext.Resolved<Nothing?>
        ): MenuComponent
    }

    fun node(): Node<MenuView>
}
