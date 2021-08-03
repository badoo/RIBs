package com.badoo.ribs.example.photo_details.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration.Content
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolution.Resolution.Companion.noop
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import kotlinx.parcelize.Parcelize

class PhotoDetailsRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: PhotoDetailsChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            object Default : Content()

            @Parcelize
            object Login : Content()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Content.Default -> noop()
                is Content.Login -> noop()
            }
        }
}

