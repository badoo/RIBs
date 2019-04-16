package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldView


@HelloWorldScope
@dagger.Component(
    modules = [HelloWorldModule::class],
    dependencies = [
        HelloWorld.Dependency::class,
        HelloWorld.Customisation::class
    ]
)
internal interface HelloWorldComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: HelloWorld.Dependency,
            customisation: HelloWorld.Customisation
        ): HelloWorldComponent
    }

    fun node(): Node<HelloWorldView>
}


