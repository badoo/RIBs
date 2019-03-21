package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView

@BlockerScope
@dagger.Component(
    modules = [BlockerModule::class],
    dependencies = [
        Blocker.Dependency::class,
        Blocker.Customisation::class
    ]
)
internal interface BlockerComponent {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: Blocker.Dependency): Builder

        fun customisation(component: Blocker.Customisation): Builder

        fun build(): BlockerComponent
    }

    fun node(): Node<BlockerView>
}
