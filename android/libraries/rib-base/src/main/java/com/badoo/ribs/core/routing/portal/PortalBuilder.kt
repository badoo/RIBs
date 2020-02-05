package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Rib

class PortalBuilder(
    override val dependency: Portal.Dependency
) : Builder<Portal.Dependency, Nothing?, PortalNode>() {

    override val rib: Rib =
        object : Portal {}

    override fun build(buildContext: BuildContext<Nothing?>): PortalNode {
        val buildContext = buildContext

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
