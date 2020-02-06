package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.builder.BuildParams
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
        buildParams: BuildParams<Nothing?>
    ): BlockerRouter =
        BlockerRouter(
            buildParams = buildParams
        )


    @BlockerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: BlockerRouter,
        output: Consumer<Output>
    ): BlockerInteractor =
        BlockerInteractor(
            buildParams = buildParams,
            router = router,
            output = output
        )

    @BlockerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Blocker.Customisation,
        router: BlockerRouter,
        interactor: BlockerInteractor
    ) : Node<BlockerView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
