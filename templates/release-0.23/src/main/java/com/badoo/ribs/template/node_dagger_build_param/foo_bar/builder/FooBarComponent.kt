package com.badoo.ribs.template.node_dagger_build_param.foo_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarNode
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
            @BindsInstance buildParams: BuildParams<FooBarBuilder.Params>
        ): FooBarComponent
    }

    fun node(): FooBarNode
}
