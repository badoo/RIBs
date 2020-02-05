package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode

class HelloWorldBuilder(
    dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency, Nothing?, HelloWorldNode>() {

    override val dependency : HelloWorld.Dependency = object : HelloWorld.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(HelloWorld::class)
    }

    override val rib: Rib =
        object : HelloWorld {}

    override fun build(buildContext: BuildContext<Nothing?>): HelloWorldNode =
        DaggerHelloWorldComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(HelloWorld.Customisation()),
                buildContext = buildContext
            )
            .node()
}
