@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.big.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.big.Big
import com.badoo.ribs.example.rib.big.BigInteractor
import com.badoo.ribs.example.rib.big.BigNode
import com.badoo.ribs.example.rib.big.BigRouter
import com.badoo.ribs.example.rib.small.builder.SmallBuilder
import dagger.Provides

@dagger.Module
internal object BigModule {

    @BigScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: BigComponent,
        savedInstanceState: Bundle?
    ): BigRouter =
        BigRouter(
            savedInstanceState = savedInstanceState,
            smallBuilder = SmallBuilder(component)
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: BigRouter
    ): BigInteractor =
        BigInteractor(
            savedInstanceState = savedInstanceState,
            router = router
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Big.Customisation,
        router: BigRouter,
        interactor: BigInteractor
    ) : BigNode = BigNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
