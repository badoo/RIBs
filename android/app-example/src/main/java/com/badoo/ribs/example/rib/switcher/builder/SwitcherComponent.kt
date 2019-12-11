package com.badoo.ribs.example.rib.switcher.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExample
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBar
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorld
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherNode
import dagger.BindsInstance


@SwitcherScope
@dagger.Component(
    modules = [SwitcherModule::class],
    dependencies = [Switcher.Dependency::class]
)
internal interface SwitcherComponent :
    MainHelloWorld.Dependency,
    MainFooBar.Dependency,
    MainDialogExample.Dependency,
    Blocker.Dependency,
    Menu.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Switcher.Dependency,
            @BindsInstance customisation: Switcher.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): SwitcherComponent
    }

    fun node(): SwitcherNode
}


