package com.badoo.ribs.tutorials.tutorial1.rib.hello_world.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
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
    internal fun router(
        savedInstanceState: Bundle?
    ): HelloWorldRouter =
        HelloWorldRouter(
            savedInstanceState = savedInstanceState
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: HelloWorldRouter,
        output: Consumer<Output>
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            output = output
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : HelloWorld {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
