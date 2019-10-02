package com.badoo.ribs.tutorials.tutorial2.rib.hello_world.builder

import com.badoo.ribs.core.BuildContext
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
    internal fun router(
        buildContext: BuildContext.Resolved<Nothing?>
    ): HelloWorldRouter =
        HelloWorldRouter(
            buildContext = buildContext
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext.Resolved<Nothing?>,
        router: HelloWorldRouter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildContext = buildContext,
            router = router
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext.Resolved<Nothing?>,
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
