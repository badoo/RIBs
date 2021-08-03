package com.badoo.ribs.example.photo_feed

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.store.get

class PhotoFeedBuilder(
    private val dependency: PhotoFeed.Dependency
) : SimpleBuilder<PhotoFeed>() {

    override fun build(buildParams: BuildParams<Nothing?>): PhotoFeed {
        val customisation = buildParams.getOrDefault(PhotoFeed.Customisation())
        val feature = RetainedInstanceStore.get(
            owner = buildParams.identifier,
            factory = { PhotoFeedFeature(dependency.dataSource) },
            disposer = { it.dispose() }
        )
        val interactor = PhotoFeedInteractor(
            buildParams = buildParams,
            feature = feature
        )

        return PhotoFeedNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
    }

}
