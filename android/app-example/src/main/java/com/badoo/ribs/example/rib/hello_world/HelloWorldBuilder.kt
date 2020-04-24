package com.badoo.ribs.example.rib.hello_world

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.builder.SmallBuilder

class HelloWorldBuilder(
    dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorldNode>() {

    private val dependency : HelloWorld.Dependency = object : HelloWorld.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(HelloWorld::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorldNode {
        val customisation = dependency.getOrDefault(HelloWorld.Customisation())
        val router = HelloWorldRouter(buildParams, smallBuilder())
        val feature = HelloWorldFeature()
        val interactor = HelloWorldInteractor(
            buildParams = buildParams,
            router = router,
            input = dependency.helloWorldInput(),
            output = dependency.helloWorldOutput(),
            feature = feature,
            activityStarter = dependency.activityStarter()
        )

        return HelloWorldNode(
            viewFactory = customisation.viewFactory(null),
            router = router,
            interactor = interactor,
            buildParams = buildParams
        )
    }

    private fun smallBuilder(): SmallBuilder =
        SmallBuilder(
            object : Small.Dependency,
                CanProvideRibCustomisation by dependency,
                CanProvidePortal by dependency { }
        )
}
