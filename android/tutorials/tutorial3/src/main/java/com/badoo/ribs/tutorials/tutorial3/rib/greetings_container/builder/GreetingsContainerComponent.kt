package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld
import dagger.BindsInstance

@GreetingsContainerScope
@dagger.Component(
    modules = [GreetingsContainerModule::class],
    dependencies = [GreetingsContainer.Dependency::class]
)
internal interface GreetingsContainerComponent : HelloWorld.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: GreetingsContainer.Dependency,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): GreetingsContainerComponent
    }

    fun node(): Node<Nothing>
}
