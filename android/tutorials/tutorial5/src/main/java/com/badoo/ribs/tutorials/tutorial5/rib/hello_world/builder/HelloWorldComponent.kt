package com.badoo.ribs.tutorials.tutorial5.rib.hello_world.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldView
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance

@HelloWorldScope
@dagger.Component(
    modules = [HelloWorldModule::class],
    dependencies = [HelloWorld.Dependency::class]
)
internal interface HelloWorldComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: HelloWorld.Dependency,
            @BindsInstance customisation: HelloWorld.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): HelloWorldComponent
    }

    fun node(): Node<HelloWorldView>
}
