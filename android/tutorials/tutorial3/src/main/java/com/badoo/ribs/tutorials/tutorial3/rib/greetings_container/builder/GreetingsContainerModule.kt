package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerInteractor
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder.HelloWorldBuilder
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object GreetingsContainerModule {

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: GreetingsContainerComponent,
        buildContext: BuildContext.Resolved<Nothing?>
    ): GreetingsContainerRouter =
        GreetingsContainerRouter(
            buildContext = buildContext,
            helloWorldBuilder = HelloWorldBuilder(component)
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext.Resolved<Nothing?>,
        router: GreetingsContainerRouter,
        output: Consumer<GreetingsContainer.Output>
    ): GreetingsContainerInteractor =
        GreetingsContainerInteractor(
            buildContext = buildContext,
            router = router,
            output = output
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext.Resolved<Nothing?>,
        router: GreetingsContainerRouter,
        interactor: GreetingsContainerInteractor
    ) : Node<Nothing> = Node(
        buildContext = buildContext,
        viewFactory = null,
        router = router,
        interactor = interactor
    )
}
