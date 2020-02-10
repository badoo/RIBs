package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder

class PortalBuilder(
    override val dependency: Portal.Dependency
) : Builder<Portal.Dependency, PortalNode>(object : Portal {}) {

    override fun build(buildParams: BuildParams<Nothing?>): PortalNode {
        val buildContext = buildParams

        val router = PortalRouter(
            buildParams = buildContext
        )

        router.defaultRoutingAction = dependency.defaultRoutingAction().invoke(router)

        val interactor = PortalInteractor(
            buildParams = buildContext,
            router = router
        )

        return PortalNode(
            buildParams = buildContext,
            router = router,
            interactor = interactor
        )
    }
}
