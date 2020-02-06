package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode

class HelloWorldBuilder(
    dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency, HelloWorldNode>() {

    override val dependency : HelloWorld.Dependency = object : HelloWorld.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(HelloWorld::class)
    }

    override val rib: Rib =
        object : HelloWorld {}

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorldNode =
        DaggerHelloWorldComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(HelloWorld.Customisation()),
                buildParams = buildParams
            )
            .node()
}
