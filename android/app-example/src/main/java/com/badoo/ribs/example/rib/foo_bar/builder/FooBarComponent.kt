package com.badoo.ribs.example.rib.foo_bar.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarView


@FooBarScope
@dagger.Component(
    modules = [FooBarModule::class],
    dependencies = [
        FooBar.Dependency::class,
        FooBar.Customisation::class
    ]
)
internal interface FooBarComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: FooBar.Dependency,
            customisation: FooBar.Customisation
        ): FooBarComponent
    }

    fun node(): Node<FooBarView>
}


