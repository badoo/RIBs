package com.badoo.ribs.example.logged_in_container.routing

import com.badoo.ribs.example.feed_container.FeedContainer
import com.badoo.ribs.example.feed_container.FeedContainerBuilder
import com.badoo.ribs.example.logged_in_container.LoggedInContainer
import com.badoo.ribs.example.photo_details.PhotoDetails
import com.badoo.ribs.example.photo_details.PhotoDetailsBuilder
import com.badoo.ribs.example.photo_details.PhotoDetailsDataSource
import com.badoo.ribs.example.photo_details.PhotoDetailsDataSourceImpl

internal class LoggedInContainerChildBuilders(
    dependency: LoggedInContainer.Dependency
) {
    private val subtreeDeps = SubtreeDeps(dependency)

    val photoFeedBuilder: FeedContainerBuilder = FeedContainerBuilder(subtreeDeps)
    val photoDetailsBuilder: PhotoDetailsBuilder = PhotoDetailsBuilder(subtreeDeps)

    private class SubtreeDeps(dependency: LoggedInContainer.Dependency) :
        LoggedInContainer.Dependency by dependency,
        FeedContainer.Dependency,
        PhotoDetails.Dependency {

        override val photoDetailsDataSource: PhotoDetailsDataSource =
            PhotoDetailsDataSourceImpl(api)
    }
}
