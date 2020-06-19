package com.badoo.ribs.example.root.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_in_container.LoggedInContainer
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.example.root.RootNode
import dagger.BindsInstance

@RootScope
@dagger.Component(
    modules = [RootModule::class],
    dependencies = [Root.Dependency::class]
)
internal interface RootComponent : LoggedInContainer.Dependency, LoggedOutContainer.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Root.Dependency,
            @BindsInstance customisation: Root.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): RootComponent
    }

    fun node(): RootNode
}
