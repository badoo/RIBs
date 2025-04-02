package com.badoo.ribs.sandbox.rib.hello_world

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.BuildConfig
import com.badoo.ribs.sandbox.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.sandbox.rib.hello_world.routing.HelloWorldChildBuilders
import com.badoo.ribs.sandbox.rib.hello_world.routing.HelloWorldRouter

class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    private val builders by lazy { HelloWorldChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld {
        val customisation = buildParams.getOrDefault(HelloWorld.Customisation())
        val feature = HelloWorldFeature()
        val interactor = HelloWorldInteractor(
            buildParams = buildParams,
            feature = feature,
            activityStarter = dependency.activityStarter
        )
        val router = HelloWorldRouter(
            buildParams = buildParams,
            builders = builders
        )

        return HelloWorldNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOfNotNull(
                interactor,
                router,
                HelloDebugControls().takeIf { BuildConfig.DEBUG }
            )
        )
    }
}
