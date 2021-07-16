package com.badoo.ribs.samples.permissions.rib.parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsSampleParentRouter.Configuration
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsSampleParentRouter.Configuration.Permanent.Child1
import com.badoo.ribs.samples.permissions.rib.parent.routing.PermissionsSampleParentRouter.Configuration.Permanent.Child2
import kotlinx.parcelize.Parcelize

class PermissionsSampleParentRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    private val builders: PermissionsSampleParentChildBuilder
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = permanent(Child1, Child2)
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            object Child1 : Permanent()

            @Parcelize
            object Child2 : Permanent()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Child1 -> child { child1Builder.build(it) }
                Child2 -> child { child2Builder.build(it) }
            }
        }
}
