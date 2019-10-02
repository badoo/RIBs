package com.badoo.ribs.example.rib.lorem_ipsum.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum.Output
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumInteractor
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumRouter
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object LoremIpsumModule {

    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: LoremIpsumComponent,
        buildContext: BuildContext.Resolved<Nothing?>
    ): LoremIpsumRouter =
        LoremIpsumRouter(
            buildContext = buildContext
        )


    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext.Resolved<Nothing?>,
        router: LoremIpsumRouter,
        output: Consumer<Output>
    ): LoremIpsumInteractor =
        LoremIpsumInteractor(
            buildContext = buildContext,
            router = router,
            output = output
        )

    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext.Resolved<Nothing?>,
        customisation: LoremIpsum.Customisation,
        router: LoremIpsumRouter,
        interactor: LoremIpsumInteractor
    ) : Node<LoremIpsumView> = Node(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
