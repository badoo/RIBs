@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.big.builder

import com.badoo.ribs.core.BuildContext
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
        buildContext: BuildContext<Nothing?>
    ): BigRouter =
        BigRouter(
            buildContext = buildContext,
            smallBuilder = SmallBuilder(component)
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext<Nothing?>,
        router: BigRouter
    ): BigInteractor =
        BigInteractor(
            buildContext = buildContext,
            router = router
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext<Nothing?>,
        customisation: Big.Customisation,
        router: BigRouter,
        interactor: BigInteractor
    ) : BigNode = BigNode(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
