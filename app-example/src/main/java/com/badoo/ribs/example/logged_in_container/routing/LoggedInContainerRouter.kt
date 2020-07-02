package com.badoo.ribs.example.logged_in_container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration.Content
import com.badoo.ribs.example.photo_details.PhotoDetailsBuilder
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class LoggedInContainerRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: LoggedInContainerChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            object PhotoFeed : Content()

            @Parcelize
            data class PhotoDetails(val photoId: String) : Content()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        with(builders) {
            when (val configuration = routing.configuration) {
                is Content.PhotoFeed -> attach { photoFeedBuilder.build(it) }
                is Content.PhotoDetails -> attach {
                    photoDetailsBuilder.build(
                        it,
                        PhotoDetailsBuilder.Params(configuration.photoId)
                    )
                }
            }
        }
}

