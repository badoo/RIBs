package com.badoo.ribs.portal.rx3

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.BasePortalBuilder
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.PortalInteractor
import com.badoo.ribs.portal.PortalRouter
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack

@ExperimentalApi
class Rx3PortalBuilder(
    dependency: Portal.Dependency
) : BasePortalBuilder<Rx3Portal>(dependency) {

    override fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter,
        backStack: BackStack<Configuration>
    ): Rx3Portal =
        Rx3PortalNode(
            buildParams = buildParams,
            backStack = backStack,
            plugins = listOf(
                interactor,
                router
            )
        )
}
