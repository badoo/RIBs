@file:SuppressWarnings("LongParameterList", "LongMethod")
package com.badoo.ribs.sandbox.rib.big.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.big.Big
import com.badoo.ribs.sandbox.rib.big.BigInteractor
import com.badoo.ribs.sandbox.rib.big.BigNode
import com.badoo.ribs.sandbox.rib.big.BigRouter
import com.badoo.ribs.sandbox.rib.small.builder.SmallBuilder
import dagger.Provides

@dagger.Module
internal object BigModule {

    @BigScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>
    ): BigInteractor =
        BigInteractor(
            buildParams = buildParams
        )

    @BigScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: BigComponent,
        buildParams: BuildParams<Nothing?>,
        interactor: BigInteractor
    ): BigRouter =
        BigRouter(
            buildParams = buildParams,
            routingSource = interactor,
            smallBuilder = SmallBuilder(component)
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
        plugins =  listOf(
            interactor, router
        )
    )
}
