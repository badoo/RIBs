package com.badoo.ribs.example.photo_feed

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature
import com.badoo.ribs.rx.disposeOnDetach

class PhotoFeedBuilder(
    private val dependency: PhotoFeed.Dependency
) : SimpleBuilder<PhotoFeed>() {

    override fun build(buildParams: BuildParams<Nothing?>): PhotoFeed {
        val customisation = buildParams.getOrDefault(PhotoFeed.Customisation())
        val feature = PhotoFeedFeature(dependency.dataSource)
        val interactor = PhotoFeedInteractor(
            buildParams = buildParams,
            feature = feature
        )

        return PhotoFeedNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor,
                disposeOnDetach(feature)
            )
        )
    }

}
