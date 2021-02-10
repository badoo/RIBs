package com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import kotlinx.android.parcel.Parcelize

class RetainedInstanceRouter internal constructor(
        buildParams: BuildParams<Nothing?>,
        routingSource: RoutingSource<Configuration>,
        private val builders: RetainedInstanceChildBuilders
) : Router<RetainedInstanceRouter.Configuration>(
        buildParams = buildParams,
        routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
            @Parcelize
            object Retained : Configuration()
            @Parcelize
            object NotRetained : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
            with(builders) {
                when (routing.configuration) {
                    Configuration.Retained -> child { retainedCounter.build(it) }
                    Configuration.NotRetained -> child { notRetainedCounter.build(it) }
                }
            }
}
