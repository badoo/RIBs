package com.badoo.ribs.core.routing.portal

import android.os.Bundle
import com.badoo.ribs.core.Builder

class PortalBuilder(
    override val dependency: Portal.Dependency
) : Builder<Portal.Dependency>() {

    fun build(savedInstanceState: Bundle?): PortalNode {
        val router = PortalRouter(
            savedInstanceState = savedInstanceState,
            transitionHandler = dependency.transitionHandler()
        )

        router.defaultRoutingAction = dependency.defaultRoutingAction().invoke(router)

        val interactor = PortalInteractor(
            savedInstanceState = savedInstanceState,
            router = router
        )

        return PortalNode(
            savedInstanceState = savedInstanceState,
            router = router,
            interactor = interactor
        )
    }
}
