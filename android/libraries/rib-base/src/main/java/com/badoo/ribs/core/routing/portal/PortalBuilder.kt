package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Builder

class PortalBuilder(
    override val dependency: Portal.Dependency
) : Builder<Portal.Dependency, Nothing?, PortalNode>() {

    override fun build(params: BuildContext.ParamsWithData<Nothing?>): PortalNode {
        val buildContext = resolve(object : Portal {}, params)

        val router = PortalRouter(
            buildContext = buildContext
        )

        router.defaultRoutingAction = dependency.defaultRoutingAction().invoke(router)

        val interactor = PortalInteractor(
            buildContext = buildContext,
            router = router
        )

        return PortalNode(
            buildContext = buildContext,
            router = router,
            interactor = interactor
        )
    }
}
