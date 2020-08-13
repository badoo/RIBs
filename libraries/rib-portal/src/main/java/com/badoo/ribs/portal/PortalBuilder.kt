package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

@ExperimentalApi
abstract class BasePortalBuilder<T : Portal>(
    private val dependency: Portal.Dependency
) : SimpleBuilder<T>() {

    override fun build(buildParams: BuildParams<Nothing?>): T {
        val interactor = PortalInteractor(
            buildParams = buildParams
        )
        val router = PortalRouter(
            buildParams = buildParams,
            routingSource = interactor,
            defaultRoutingAction = dependency.defaultRoutingAction().invoke(interactor),
            transitionHandler = dependency.transitionHandler()
        )

        return createNode(buildParams, interactor, router)
    }

    protected abstract fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter
    ): T
}

class PortalBuilder(dependency: Portal.Dependency): BasePortalBuilder<Portal>(dependency) {
    override fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter
    ): Portal =
        PortalNode(
            buildParams = buildParams,
            plugins = listOf(
                interactor,
                router
            )
        )
}