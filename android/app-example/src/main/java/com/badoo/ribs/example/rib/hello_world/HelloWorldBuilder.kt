package com.badoo.ribs.example.rib.hello_world

import android.os.Bundle
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.builder.SmallBuilder

class HelloWorldBuilder(
    dependency: HelloWorld.Dependency
) : Builder<HelloWorld.Dependency>() {

    override val dependency : HelloWorld.Dependency = object : HelloWorld.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(HelloWorld::class)
    }

    fun build(savedInstanceState: Bundle?): HelloWorldNode {
        val customisation = dependency.getOrDefault(HelloWorld.Customisation())
        val router = HelloWorldRouter(savedInstanceState, smallBuilder())
        val feature = HelloWorldFeature()
        val interactor = HelloWorldInteractor(
            savedInstanceState,
            router,
            dependency.helloWorldInput(),
            dependency.helloWorldOutput(),
            feature,
            dependency.activityStarter()
        )

        return HelloWorldNode(
            customisation.viewFactory(null),
            router,
            interactor,
            savedInstanceState
        )
    }

    private fun smallBuilder(): SmallBuilder =
        SmallBuilder(
            object : Small.Dependency,
                CanProvideRibCustomisation by dependency,
                CanProvidePortal by dependency { }
        )
}
