package com.badoo.ribs.samples.back_stack.parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.A
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.B
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.C
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.D
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Overlay.E
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Overlay.F
import kotlinx.android.parcel.Parcelize

class ParentRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val builders: ParentChildBuilders
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object A : Configuration()
            @Parcelize object B : Configuration()
            @Parcelize object C : Configuration()
            @Parcelize object D : Configuration()
        }

        sealed class Overlay : Configuration() {
            @Parcelize object E : Configuration()
            @Parcelize object F : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                A -> child { childA.build(it) }
                B -> child { childB.build(it) }
                C -> child { childC.build(it) }
                D -> child { childD.build(it) }
                E -> child { childE.build(it) }
                F -> child { childF.build(it) }
            }
        }

}
