@file:Suppress("LongParameterList")
package com.badoo.ribs.tutorials.tutorial4.rib.hello_world.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorldView
import com.badoo.ribs.tutorials.tutorial4.util.User
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        buildContext: BuildContext<Nothing?>
    ): HelloWorldRouter =
        HelloWorldRouter(
            buildContext = buildContext
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext<Nothing?>,
        user: User,
        config: HelloWorld.Config,
        output: Consumer<HelloWorld.Output>,
        router: HelloWorldRouter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildContext = buildContext,
            user = user,
            config = config,
            output = output,
            router = router
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext<Nothing?>,
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
