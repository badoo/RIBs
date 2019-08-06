package com.badoo.ribs.example.rib.blocker.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
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
        savedInstanceState: Bundle?
    ): BlockerRouter =
        BlockerRouter(
            savedInstanceState = savedInstanceState
        )


    @BlockerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: BlockerRouter,
        output: Consumer<Output>
    ): BlockerInteractor =
        BlockerInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            output = output
        )

    @BlockerScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Blocker.Customisation,
        router: BlockerRouter,
        interactor: BlockerInteractor
    ) : Node<BlockerView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : Blocker {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
