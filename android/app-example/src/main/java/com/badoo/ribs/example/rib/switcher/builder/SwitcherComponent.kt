package com.badoo.ribs.example.rib.switcher.builder

import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherView
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.dialog_example.DialogExample


@SwitcherScope
@dagger.Component(
    modules = [SwitcherModule::class],
    dependencies = [
        Switcher.Dependency::class,
        Switcher.Customisation::class
    ]
)
internal interface SwitcherComponent :
    HelloWorld.Dependency,
    FooBar.Dependency,
    DialogExample.Dependency,
    Menu.Dependency {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: Switcher.Dependency): Builder

        fun customisation(component: Switcher.Customisation): Builder

        fun build(): SwitcherComponent
    }

    fun node(): Node<SwitcherView>
}


