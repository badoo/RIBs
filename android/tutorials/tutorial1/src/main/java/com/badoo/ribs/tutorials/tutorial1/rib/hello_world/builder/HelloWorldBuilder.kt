package com.badoo.ribs.tutorials.tutorial1.rib.hello_world.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldView

class HelloWorldBuilder(
    override val dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency>() {

    fun build(): Node<HelloWorldView> =
        DaggerHelloWorldComponent
            .factory()
            .create(dependency, HelloWorld.Customisation())
            .node()
}
