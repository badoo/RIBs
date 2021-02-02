package com.badoo.ribs.samples.buildtime.parent.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.buildtime.parent.BuildTimeDepsParent
import com.badoo.ribs.samples.buildtime.parent.BuildTimeDepsParentInteractor
import com.badoo.ribs.samples.buildtime.parent.BuildTimeDepsParentNode
import com.badoo.ribs.samples.buildtime.parent.BuildTimeDepsParentViewImpl
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter
import com.badoo.ribs.samples.buildtime.profile.builder.BuildTimeDepsProfileBuilder

class BuildTimeDepsParentBuilder : SimpleBuilder<BuildTimeDepsParent>() {

    override fun build(buildParams: BuildParams<Nothing?>): BuildTimeDepsParent {
        val interactor = BuildTimeDepsParentInteractor(
            buildParams = buildParams,
            backStack = BackStack(
                initialConfiguration = BuildTimeDepsParentRouter.Configuration.Default,
                buildParams = buildParams
            )
        )
        val router = BuildTimeDepsParentRouter(
            buildParams = buildParams,
            routingSource = interactor,
            profileBuilder = BuildTimeDepsProfileBuilder()
        )
        return BuildTimeDepsParentNode(
            buildParams = buildParams,
            viewFactory = BuildTimeDepsParentViewImpl.Factory().invoke(null),
            plugins = listOf(interactor, router)
        )
    }
}
