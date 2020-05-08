package com.badoo.ribs.sandbox.rib.switcher.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.blocker.Blocker
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.switcher.Switcher
import com.badoo.ribs.sandbox.rib.switcher.SwitcherNode
import dagger.BindsInstance


@SwitcherScope
@dagger.Component(
    modules = [SwitcherModule::class],
    dependencies = [Switcher.Dependency::class]
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
            @BindsInstance customisation: Switcher.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): SwitcherComponent
    }

    fun node(): SwitcherNode
}


