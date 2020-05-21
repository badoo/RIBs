package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerInteractor
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder.HelloWorldBuilder
import dagger.Provides

@dagger.Module
internal object GreetingsContainerModule {

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>
    ): GreetingsContainerInteractor =
        GreetingsContainerInteractor(
            buildParams = buildParams
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: GreetingsContainerComponent,
        interactor: GreetingsContainerInteractor,
        buildParams: BuildParams<Nothing?>
    ): GreetingsContainerRouter =
        GreetingsContainerRouter(
            buildParams = buildParams,
            routingSource = interactor,
            helloWorldBuilder = HelloWorldBuilder(component)
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        router: GreetingsContainerRouter,
        interactor: GreetingsContainerInteractor
    ) : Node<Nothing> = Node(
        buildParams = buildParams,
        viewFactory = null,
        plugins = listOf(interactor, router)
    )
}
