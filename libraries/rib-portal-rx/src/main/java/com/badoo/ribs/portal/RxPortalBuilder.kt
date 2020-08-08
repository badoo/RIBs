package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

@ExperimentalApi
class RxPortalBuilder(
    private val dependency: Portal.Dependency
) : SimpleBuilder<RxPortal>() {

    override fun build(buildParams: BuildParams<Nothing?>): RxPortal {
        val interactor = PortalInteractor(
            buildParams = buildParams
        )
        val router = PortalRouter(
            buildParams = buildParams,
            routingSource = interactor,
            defaultRoutingAction = dependency.defaultRoutingAction().invoke(interactor),
            transitionHandler = dependency.transitionHandler()
        )

        return RxPortalNode(
            buildParams = buildParams,
            plugins = listOf(
                interactor,
                router
            ),
            backStack = interactor.backStack
        )
    }
}
