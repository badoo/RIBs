package com.badoo.ribs.sandbox.rib.hello_world.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.attach
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.sandbox.rib.hello_world.routing.HelloWorldRouter.Configuration
import com.badoo.ribs.sandbox.rib.hello_world.routing.HelloWorldRouter.Configuration.Permanent.Small
import kotlinx.android.parcel.Parcelize

class HelloWorldRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    private val builders: HelloWorldChildBuilders
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = permanent(Small)
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Small : Permanent()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Small -> attach { small.build(it) }
            }
        }
}
