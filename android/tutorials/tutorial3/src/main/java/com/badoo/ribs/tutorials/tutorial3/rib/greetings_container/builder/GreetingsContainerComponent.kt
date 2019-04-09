package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerView
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld

@GreetingsContainerScope
@dagger.Component(
    modules = [GreetingsContainerModule::class],
    dependencies = [
        GreetingsContainer.Dependency::class,
        GreetingsContainer.Customisation::class
    ]
)
internal interface GreetingsContainerComponent : HelloWorld.Dependency {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: GreetingsContainer.Dependency): Builder

        fun customisation(component: GreetingsContainer.Customisation): Builder

        fun build(): GreetingsContainerComponent
    }

    fun node(): Node<GreetingsContainerView>
}
