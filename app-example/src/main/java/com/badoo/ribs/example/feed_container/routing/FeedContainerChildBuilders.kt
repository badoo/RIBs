package com.badoo.ribs.example.feed_container.routing

import com.badoo.ribs.example.feed_container.FeedContainer
import com.badoo.ribs.example.photo_feed.PhotoFeed
import com.badoo.ribs.example.photo_feed.PhotoFeedBuilder
import com.badoo.ribs.example.photo_feed.PhotoFeedDataSource
import com.badoo.ribs.example.photo_feed.PhotoFeedDataSourceImpl

internal class FeedContainerChildBuilders(
    dependency: FeedContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val photoFeedBuilder = PhotoFeedBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: FeedContainer.Dependency
    ) : FeedContainer.Dependency by dependency, PhotoFeed.Dependency {
        override val dataSource: PhotoFeedDataSource = PhotoFeedDataSourceImpl(api)
    }
}



