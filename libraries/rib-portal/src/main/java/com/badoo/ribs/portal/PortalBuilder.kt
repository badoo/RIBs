package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack

@ExperimentalApi
abstract class BasePortalBuilder<T : Portal>(
    private val dependency: Portal.Dependency
) : SimpleBuilder<T>() {

    override fun build(buildParams: BuildParams<Nothing?>): T {
        val backStack = BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Content.Default
        )
        val interactor = PortalInteractor(
            buildParams = buildParams,
            backStack = backStack
        )
        val router = PortalRouter(
            buildParams = buildParams,
            routingSource = backStack,
            defaultResolution = dependency.defaultResolution.invoke(interactor),
            transitionHandler = dependency.transitionHandler
        )

        return createNode(
            buildParams = buildParams,
            interactor = interactor,
            router = router,
            backStack = backStack
        )
    }

    protected abstract fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter,
        backStack: BackStack<Configuration>
    ): T
}

class PortalBuilder(dependency: Portal.Dependency) : BasePortalBuilder<Portal>(dependency) {
    override fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter,
        backStack: BackStack<Configuration>
    ): Portal =
        PortalNode(
            buildParams = buildParams,
            plugins = listOf(
                interactor,
                router
            )
        )
}
