package com.badoo.ribs.samples.parameterised_routing.rib.parameterised_routing_example

import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.parameterised_routing.rib.parameterised_routing_example.ParameterisedRoutingExampleRouter.Configuration.ShowProfile

interface ParameterisedRoutingExamplePresenter {

    fun onBuildChildClicked(profileId: Int)
}

internal class ParameterisedRoutingExamplePresenterImpl(
    private val backStack: BackStack<ParameterisedRoutingExampleRouter.Configuration>
) : ViewAware<ParameterisedRoutingExampleView>, ParameterisedRoutingExamplePresenter {

    override fun onBuildChildClicked(profileId: Int) {
        backStack.replace(ShowProfile(profileId))
    }
}
