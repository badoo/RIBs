package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView
import dagger.BindsInstance

@BlockerScope
@dagger.Component(
    modules = [BlockerModule::class],
    dependencies = [Blocker.Dependency::class]
)
internal interface BlockerComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Blocker.Dependency,
            @BindsInstance customisation: Blocker.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): BlockerComponent
    }

    fun node(): Node<BlockerView>
}
