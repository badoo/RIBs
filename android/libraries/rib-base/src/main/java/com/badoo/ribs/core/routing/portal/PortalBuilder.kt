package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class PortalBuilder(
    private val dependency: Portal.Dependency
) : SimpleBuilder<Portal>() {

    override fun build(buildParams: BuildParams<Nothing?>): Portal {
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
