package com.badoo.ribs.example.rib.foo_bar.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarNode
import dagger.BindsInstance


@FooBarScope
@dagger.Component(
    modules = [FooBarModule::class],
    dependencies = [FooBar.Dependency::class]
)
internal interface FooBarComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: FooBar.Dependency,
            @BindsInstance customisation: FooBar.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): FooBarComponent
    }

    fun node(): FooBarNode
}

