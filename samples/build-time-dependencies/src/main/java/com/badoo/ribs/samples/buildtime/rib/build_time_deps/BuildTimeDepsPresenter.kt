package com.badoo.ribs.samples.buildtime.rib.build_time_deps

import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.buildtime.rib.build_time_deps.BuildTimeDepsRouter.Configuration.ShowProfile

interface BuildTimeDepsPresenter {

    fun onBuildChildClicked(profileId: Int)
}

internal class BuildTimeDepsPresenterImpl(
    private val backStack: BackStack<BuildTimeDepsRouter.Configuration>
) : ViewAware<BuildTimeDepsView>, BuildTimeDepsPresenter {

    override fun onBuildChildClicked(profileId: Int) {
        backStack.replace(ShowProfile(profileId))
    }
}
