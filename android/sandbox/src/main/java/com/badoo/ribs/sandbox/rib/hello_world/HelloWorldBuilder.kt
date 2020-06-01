package com.badoo.ribs.sandbox.rib.hello_world

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.sandbox.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.sandbox.rib.small.Small
import com.badoo.ribs.sandbox.rib.small.builder.SmallBuilder

class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld {
        val customisation = buildParams.getOrDefault(HelloWorld.Customisation())
        val feature = HelloWorldFeature()
        val interactor = HelloWorldInteractor(
            buildParams = buildParams,
            feature = feature,
            activityStarter = dependency.activityStarter()
        )
        val router = HelloWorldRouter(
            buildParams,
            interactor,
            smallBuilder()
        )

        return HelloWorldNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins =  listOf(
                interactor,
                router
            )
        )
    }

    private fun smallBuilder(): SmallBuilder =
        SmallBuilder(
            object : Small.Dependency,
                CanProvidePortal by dependency { }
        )
}
