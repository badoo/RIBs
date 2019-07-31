package com.badoo.ribs.example.rib.hello_world.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldView

class HelloWorldBuilder(
    dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency>() {

    override val dependency : HelloWorld.Dependency = object : HelloWorld.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(HelloWorld::class)
    }

    fun build(savedInstanceState: Bundle?): Node<HelloWorldView> =
        DaggerHelloWorldComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(HelloWorld.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
