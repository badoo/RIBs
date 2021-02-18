package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack

@ExperimentalApi
class RxPortalBuilder(
    dependency: Portal.Dependency
) : BasePortalBuilder<RxPortal>(dependency) {

    override fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter,
        backStack: BackStack<Configuration>
    ): RxPortal =
        RxPortalNode(
            buildParams = buildParams,
            backStack = backStack,
            plugins = listOf(
                interactor,
                router
            )
        )
}
