package com.badoo.ribs.tutorials.tutorial1.rib.hello_world.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldView
import dagger.Provides
import io.reactivex.functions.Consumer

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
        router: HelloWorldRouter,
        output: Consumer<Output>
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            router = router,
            output = output
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        viewFactory: ViewFactory<HelloWorldView>,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        identifier = object : HelloWorld {},
        viewFactory = viewFactory,
        router = router,
        interactor = interactor
    )
}
