package com.badoo.ribs.example.rib.root.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.root.Root
import com.badoo.ribs.example.rib.root.RootNode
import com.badoo.ribs.example.rib.switcher.Switcher
import dagger.BindsInstance

@RootScope
@dagger.Component(
    modules = [RootModule::class],
    dependencies = [Root.Dependency::class]
)
internal interface RootComponent : Switcher.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Root.Dependency,
            @BindsInstance customisation: Root.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): RootComponent
    }

    fun node(): RootNode
}
