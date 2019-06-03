package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainer

@GreetingsContainerScope
@dagger.Component(
    modules = [GreetingsContainerModule::class],
    dependencies = [
        GreetingsContainer.Dependency::class
    ]
)
internal interface GreetingsContainerComponent {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: GreetingsContainer.Dependency): Builder

        fun build(): GreetingsContainerComponent
    }

    fun node(): Node<Nothing>
}
