package com.badoo.ribs.example.photo_details

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsChildBuilders
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack

class PhotoDetailsBuilder(
    private val dependency: PhotoDetails.Dependency
) : Builder<PhotoDetailsBuilder.Params, PhotoDetails>() {

    data class Params(val photoId: String)

    @SuppressWarnings("LongMethod")
    override fun build(buildParams: BuildParams<Params>): PhotoDetails {
        val connections = PhotoDetailsChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(PhotoDetails.Customisation())
        val backStack = BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Configuration.Content.Default
        )
        val feature = PhotoDetailsFeature(
            buildParams.payload.photoId,
            dependency.photoDetailsDataSource
        )
        val interactor = PhotoDetailsInteractor(
            buildParams = buildParams,
            backStack = backStack,
            feature = feature
        )
        val router = PhotoDetailsRouter(
            buildParams = buildParams,
            builders = connections,
            routingSource = backStack,
            transitionHandler = customisation.transitionHandler
        )

        return PhotoDetailsNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor, router)
        )
    }

}
