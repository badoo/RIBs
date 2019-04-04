package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.Blocker.Output
import com.badoo.ribs.example.rib.blocker.BlockerInteractor
import com.badoo.ribs.example.rib.blocker.BlockerRouter
import com.badoo.ribs.example.rib.blocker.BlockerView
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object BlockerModule {

    @BlockerScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: BlockerComponent
    ): BlockerRouter =
        BlockerRouter()


    @BlockerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        router: BlockerRouter,
        output: Consumer<Output>
    ): BlockerInteractor =
        BlockerInteractor(
            router = router,
            output = output
        )

    @BlockerScope
    @Provides
    @JvmStatic
    internal fun node(
        viewFactory: ViewFactory<BlockerView>,
        router: BlockerRouter,
        interactor: BlockerInteractor
    ) : Node<BlockerView> = Node(
        identifier = object : Blocker {},
        viewFactory = viewFactory,
        router = router,
        interactor = interactor
    )
}
