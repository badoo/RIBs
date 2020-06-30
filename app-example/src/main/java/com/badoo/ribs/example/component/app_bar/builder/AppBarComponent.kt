package com.badoo.ribs.example.component.app_bar.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.AppBar
import com.badoo.ribs.example.component.app_bar.AppBarNode
import com.badoo.ribs.example.component.app_bar.builder.AppBarBuilder.Params
import dagger.BindsInstance

@AppBarScope
@dagger.Component(
    modules = [AppBarModule::class],
    dependencies = [AppBar.Dependency::class]
)
internal interface AppBarComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: AppBar.Dependency,
            @BindsInstance customisation: AppBar.Customisation,
            @BindsInstance buildParams: BuildParams<Params>
        ): AppBarComponent
    }

    fun node(): AppBarNode
}
