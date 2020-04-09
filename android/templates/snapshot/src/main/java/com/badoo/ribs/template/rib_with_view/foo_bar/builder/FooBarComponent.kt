package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarNode
import dagger.BindsInstance

@FooBarScope
@dagger.Component(
    modules = [FooBarModule::class]
)
internal interface FooBarComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            @BindsInstance dependency: FooBar.Dependency,
            @BindsInstance customisation: FooBar.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): FooBarComponent
    }

    fun node(): FooBarNode
}
