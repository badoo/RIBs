package com.badoo.ribs.tutorials.tutorial2.rib.hello_world.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldView

class HelloWorldBuilder(
    override val dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency, Nothing?, Node<HelloWorldView>>() {

    override fun build(buildContext: BuildContext<Nothing?>): Node<HelloWorldView> =
        DaggerHelloWorldComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = HelloWorld.Customisation(),
                buildContext = buildContext
            )
            .node()
}
