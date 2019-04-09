package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerView

@GreetingsContainerScope
@dagger.Component(
    modules = [GreetingsContainerModule::class],
    dependencies = [
        GreetingsContainer.Dependency::class,
        GreetingsContainer.Customisation::class
    ]
)
internal interface GreetingsContainerComponent {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: GreetingsContainer.Dependency): Builder

        fun customisation(component: GreetingsContainer.Customisation): Builder

        fun build(): GreetingsContainerComponent
    }

    fun node(): Node<GreetingsContainerView>
}
