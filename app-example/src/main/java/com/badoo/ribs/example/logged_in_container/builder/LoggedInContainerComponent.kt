package com.badoo.ribs.example.logged_in_container.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_in_container.LoggedInContainer
import com.badoo.ribs.example.logged_in_container.LoggedInContainerNode
import dagger.BindsInstance

@LoggedInContainerScope
@dagger.Component(
    modules = [LoggedInContainerModule::class],
    dependencies = [LoggedInContainer.Dependency::class]
)
internal interface LoggedInContainerComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: LoggedInContainer.Dependency,
            @BindsInstance customisation: LoggedInContainer.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): LoggedInContainerComponent
    }

    fun node(): LoggedInContainerNode
}
