package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.retained_instance_store.rib.counter.Counter
import com.badoo.ribs.samples.retained_instance_store.rib.counter.CounterBuilder
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleRouter.Configuration.Content
import kotlinx.android.parcel.Parcelize

class RetainedInstanceExampleRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val counterBuilder: CounterBuilder
) : Router<RetainedInstanceExampleRouter.Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            object Retained : Configuration()

            @Parcelize
            object NotRetained : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            Content.Retained -> child { counterBuilder.build(it, Counter.Params(true)) }
            Content.NotRetained -> child { counterBuilder.build(it, Counter.Params(false)) }
        }
}


