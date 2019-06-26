package com.badoo.ribs.tutorials.tutorial2.rib.hello_world.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldView
import dagger.Provides

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(): HelloWorldRouter =
        HelloWorldRouter()

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        router: HelloWorldRouter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            router = router
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        identifier = object : HelloWorld {},
        viewFactory = customisation.viewFactory,
        router = router,
        interactor = interactor
    )
}
