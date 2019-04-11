package com.badoo.ribs.example.rib.switcher.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherView


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
    Blocker.Dependency,
    Menu.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Switcher.Dependency,
            customisation: Switcher.Customisation
        ): SwitcherComponent
    }

    fun node(): Node<SwitcherView>
}


