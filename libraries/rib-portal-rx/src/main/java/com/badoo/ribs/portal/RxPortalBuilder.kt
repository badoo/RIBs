package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.modality.BuildParams

@ExperimentalApi
class RxPortalBuilder(
    dependency: Portal.Dependency
) : BasePortalBuilder<RxPortal>(dependency) {

    override fun createNode(
        buildParams: BuildParams<Nothing?>,
        interactor: PortalInteractor,
        router: PortalRouter
    ): RxPortal =
        RxPortalNode(
            buildParams = buildParams,
            plugins = listOf(
                interactor,
                router
            ),
            backStack = interactor.backStack
        )
}
