package com.badoo.ribs.samples.comms_nodes_1.rib.container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerRouter.Configuration
import com.badoo.ribs.samples.comms_nodes_1.rib.container.routing.ContainerRouter.Configuration.Permanent.Menu
import kotlinx.android.parcel.Parcelize

class ContainerRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    private val builders: ContainerChildBuilders
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = permanent(Menu)
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Menu : Permanent()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Menu -> child { menu.build(it) }
            }
        }
}
