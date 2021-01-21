package com.samples.back_stack.parent.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.samples.back_stack.parent.routing.ParentRouter.Configuration
import com.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.A
import com.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.B
import com.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.C
import com.samples.back_stack.parent.routing.ParentRouter.Configuration.Content.D
import com.samples.back_stack.parent.routing.ParentRouter.Configuration.Overlay.E
import com.samples.back_stack.parent.routing.ParentRouter.Configuration.Overlay.F
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
        when (routing.configuration) {
            A -> child { builders.contentA.build(it) }
            B -> child { builders.contentB.build(it) }
            C -> child { builders.contentC.build(it) }
            D -> child { builders.contentD.build(it) }
            E -> child { builders.overlayE.build(it) }
            F -> child { builders.overlayF.build(it) }
        }

}
