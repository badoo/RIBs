package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.BuildContext
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
        buildContext: BuildContext<Nothing?>
    ): BlockerRouter =
        BlockerRouter(
            buildContext = buildContext
        )


    @BlockerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext<Nothing?>,
        router: BlockerRouter,
        output: Consumer<Output>
    ): BlockerInteractor =
        BlockerInteractor(
            buildContext = buildContext,
            router = router,
            output = output
        )

    @BlockerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext<Nothing?>,
        customisation: Blocker.Customisation,
        router: BlockerRouter,
        interactor: BlockerInteractor
    ) : Node<BlockerView> = Node(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
