package com.badoo.ribs.template.leaf_dagger.foo_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.template.leaf_dagger.foo_bar.builder.FooBarModule
import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBar
import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBarNode
import com.badoo.ribs.template.leaf_dagger.foo_bar.builder.FooBarBuilder.Params
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
            @BindsInstance buildParams: BuildParams<Params>
        ): FooBarComponent
    }

    fun node(): FooBarNode
}
