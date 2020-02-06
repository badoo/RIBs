package com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorldView

class HelloWorldBuilder(
    override val dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency, Nothing?, Node<HelloWorldView>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<HelloWorldView> =
        DaggerHelloWorldComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = HelloWorld.Customisation(),
                buildContext = buildParams
            )
            .node()
}
