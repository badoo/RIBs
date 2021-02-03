package com.badoo.ribs.samples.buildtime.rib.build_time_deps

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.buildtime.rib.build_time_deps.BuildTimeDepsRouter.Configuration.ShowProfile

internal class BuildTimeDepsPresenter(
    private val backStack: BackStack<BuildTimeDepsRouter.Configuration>
) : ViewAware<BuildTimeDepsView> {

    override fun onViewCreated(view: BuildTimeDepsView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                view.setBuildChildListener { profileId ->
                    backStack.replace(ShowProfile(profileId))
                }
            },
            onDestroy = {
                view.setBuildChildListener(null)
            }
        )
    }
}
