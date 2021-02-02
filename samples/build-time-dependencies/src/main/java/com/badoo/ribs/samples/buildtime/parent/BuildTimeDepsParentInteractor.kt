package com.badoo.ribs.samples.buildtime.parent

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter.Configuration.Default
import com.badoo.ribs.samples.buildtime.parent.routing.BuildTimeDepsParentRouter.Configuration.ShowProfile

internal class BuildTimeDepsParentInteractor(
    buildParams: BuildParams<Nothing?>,
    private val backStack: BackStack<BuildTimeDepsParentRouter.Configuration>
) : Interactor<BuildTimeDepsParent, BuildTimeDepsParentView>(
    buildParams = buildParams
), RoutingSource<BuildTimeDepsParentRouter.Configuration> by backStack {

    override fun onViewCreated(view: BuildTimeDepsParentView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                view.setBuildChildListener { profileId ->
                    if (profileId != null) {
                        backStack.replace(ShowProfile(profileId))
                    } else {
                        // Typically occurs when an invalid integer was input.
                        backStack.replace(Default)
                    }
                }
            },
            onDestroy = {
                view.setBuildChildListener(null)
            }
        )
    }
}
