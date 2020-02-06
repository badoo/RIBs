package com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorldView
import dagger.Provides

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>
    ): HelloWorldRouter =
        HelloWorldRouter(
            buildParams = buildParams
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: HelloWorldRouter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildParams = buildParams,
            router = router
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
