package com.badoo.ribs.example.logged_out_container.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer
import com.badoo.ribs.example.logged_out_container.LoggedOutContainerNode
import dagger.BindsInstance

@LoggedOutContainerScope
@dagger.Component(
    modules = [LoggedOutContainerModule::class],
    dependencies = [LoggedOutContainer.Dependency::class]
)
internal interface LoggedOutContainerComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: LoggedOutContainer.Dependency,
            @BindsInstance customisation: LoggedOutContainer.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): LoggedOutContainerComponent
    }

    fun node(): LoggedOutContainerNode
}
