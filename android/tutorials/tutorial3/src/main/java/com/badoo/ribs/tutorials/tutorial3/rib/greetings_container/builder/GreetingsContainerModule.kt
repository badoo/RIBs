package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerInteractor
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object GreetingsContainerModule {

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: GreetingsContainerComponent
    ): GreetingsContainerRouter =
        GreetingsContainerRouter()

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        router: GreetingsContainerRouter,
        output: Consumer<GreetingsContainer.Output>
    ): GreetingsContainerInteractor =
        GreetingsContainerInteractor(
            router = router,
            output = output
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        router: GreetingsContainerRouter,
        interactor: GreetingsContainerInteractor
    ) : Node<Nothing> = Node(
        identifier = object : GreetingsContainer {},
        viewFactory = null,
        router = router,
        interactor = interactor
    )
}
