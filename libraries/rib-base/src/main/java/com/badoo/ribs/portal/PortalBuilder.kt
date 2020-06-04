package com.badoo.ribs.portal

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class PortalBuilder(
    private val dependency: Portal.Dependency
) : SimpleBuilder<Portal>() {

    override fun build(buildParams: BuildParams<Nothing?>): Portal {
        val interactor = PortalInteractor(
            buildParams = buildParams
        )
        val router = PortalRouter(
            buildParams = buildParams,
            routingSource = interactor,
            defaultRoutingAction = dependency.defaultRoutingAction().invoke(interactor),
            transitionHandler = dependency.transitionHandler()
        )

        return PortalNode(
            buildParams = buildParams,
            plugins = listOf(
                interactor,
                router
            ),
            backStack = interactor.backStack
        )
    }
}
