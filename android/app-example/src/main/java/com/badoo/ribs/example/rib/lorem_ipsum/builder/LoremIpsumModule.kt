package com.badoo.ribs.example.rib.lorem_ipsum.builder

import android.os.Bundle
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
        savedInstanceState: Bundle?
    ): LoremIpsumRouter =
        LoremIpsumRouter(
            savedInstanceState = savedInstanceState
        )


    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: LoremIpsumRouter,
        output: Consumer<Output>
    ): LoremIpsumInteractor =
        LoremIpsumInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            output = output
        )

    @LoremIpsumScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: LoremIpsum.Customisation,
        router: LoremIpsumRouter,
        interactor: LoremIpsumInteractor
    ) : Node<LoremIpsumView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : LoremIpsum {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
