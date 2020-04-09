@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.example.rib.big.builder

import com.badoo.ribs.core.builder.BuildParams
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
        buildParams: BuildParams<Nothing?>
    ): BigRouter =
        BigRouter(
            buildParams = buildParams,
            smallBuilder = SmallBuilder(component)
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: BigRouter
    ): BigInteractor =
        BigInteractor(
            buildParams = buildParams,
            router = router
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Big.Customisation,
        router: BigRouter,
        interactor: BigInteractor
    ) : BigNode = BigNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
