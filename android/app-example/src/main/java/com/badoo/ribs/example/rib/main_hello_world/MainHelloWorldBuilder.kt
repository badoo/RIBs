package com.badoo.ribs.example.rib.main_hello_world

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.main_hello_world.feature.MainHelloWorldFeature
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import com.badoo.ribs.example.rib.portal_sub_screen.builder.PortalSubScreenBuilder

class MainHelloWorldBuilder(
    dependency: MainHelloWorld.Dependency
) : Builder<MainHelloWorld.Dependency>() {

    override val dependency : MainHelloWorld.Dependency = object : MainHelloWorld.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(MainHelloWorld::class)
    }

    fun build(savedInstanceState: Bundle?): MainHelloWorldNode {
        val customisation = dependency.getOrDefault(MainHelloWorld.Customisation())
        val router = MainHelloWorldRouter(savedInstanceState, smallBuilder())
        val feature = MainHelloWorldFeature()
        val interactor = MainHelloWorldInteractor(
            savedInstanceState,
            router,
            dependency.helloWorldInput(),
            dependency.helloWorldOutput(),
            feature,
            dependency.activityStarter()
        )

        return MainHelloWorldNode(
            customisation.viewFactory(null),
            router,
            interactor,
            savedInstanceState
        )
    }

    private fun smallBuilder(): PortalSubScreenBuilder =
        PortalSubScreenBuilder(
            object : PortalSubScreen.Dependency,
                CanProvideRibCustomisation by dependency,
                CanProvidePortal by dependency { }
        )
}
