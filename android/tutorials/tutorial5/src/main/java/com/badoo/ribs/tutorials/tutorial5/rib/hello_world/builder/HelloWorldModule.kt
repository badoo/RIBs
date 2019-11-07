package com.badoo.ribs.tutorials.tutorial5.rib.hello_world.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldView
import com.badoo.ribs.tutorials.tutorial5.util.User
import dagger.Provides
import io.reactivex.ObservableSource
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
    @SuppressWarnings("LongParameterList", "LongMethod")
    internal fun interactor(
        savedInstanceState: Bundle?,
        user: User,
        config: HelloWorld.Config,
        input: ObservableSource<HelloWorld.Input>,
        output: Consumer<HelloWorld.Output>,
        router: HelloWorldRouter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            savedInstanceState = savedInstanceState,
            user = user,
            config = config,
            input = input,
            output = output,
            router = router
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

