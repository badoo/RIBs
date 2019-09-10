package com.badoo.ribs.example.rib.hello_world.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode
import com.badoo.ribs.example.rib.small.Small
import dagger.BindsInstance


@HelloWorldScope
@dagger.Component(
    modules = [HelloWorldModule::class],
    dependencies = [HelloWorld.Dependency::class]
)
internal interface HelloWorldComponent : Small.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: HelloWorld.Dependency,
            @BindsInstance customisation: HelloWorld.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): HelloWorldComponent
    }

    fun node(): HelloWorldNode
}


