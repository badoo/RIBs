package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView
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
            @BindsInstance customisation: FooBar.Customisation
        ): FooBarComponent
    }

    fun node(): Node<FooBarView>
}
