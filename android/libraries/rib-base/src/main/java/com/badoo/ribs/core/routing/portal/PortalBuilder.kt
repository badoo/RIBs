package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder

class PortalBuilder(
    override val dependency: Portal.Dependency
) : Builder<Portal.Dependency, PortalNode>(object : Portal {}) {

    override fun build(buildParams: BuildParams<Nothing?>): PortalNode {
        val router = PortalRouter(
            buildParams = buildParams,
            transitionHandler = dependency.transitionHandler()
        )

        router.defaultRoutingAction = dependency.defaultRoutingAction().invoke(router)

        val interactor = PortalInteractor(
            buildParams = buildParams,
            router = router
        )

        return PortalNode(
            buildParams = buildParams,
            router = router,
            interactor = interactor
        )
    }
}
