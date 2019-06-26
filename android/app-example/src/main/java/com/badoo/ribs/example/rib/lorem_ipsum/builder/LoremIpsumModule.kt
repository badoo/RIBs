package com.badoo.ribs.example.rib.lorem_ipsum.builder

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
        component: LoremIpsumComponent
    ): LoremIpsumRouter =
        LoremIpsumRouter()


    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun interactor(
        router: LoremIpsumRouter,
        output: Consumer<Output>
    ): LoremIpsumInteractor =
        LoremIpsumInteractor(
            router = router,
            output = output
        )

    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun node(
        customisation: LoremIpsum.Customisation,
        router: LoremIpsumRouter,
        interactor: LoremIpsumInteractor
    ) : Node<LoremIpsumView> = Node(
        identifier = object : LoremIpsum {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
