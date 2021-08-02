package com.badoo.ribs.example.feed_container.mapper

import com.badoo.ribs.example.feed_container.FeedContainer
import com.badoo.ribs.example.photo_feed.PhotoFeed

internal object FeedOutputToContainerOutput : (PhotoFeed.Output) -> FeedContainer.Output? {
    override fun invoke(output: PhotoFeed.Output): FeedContainer.Output? =
        when (output) {
            is PhotoFeed.Output.PhotoClicked -> FeedContainer.Output.PhotoClicked(output.photo.id)
        }
}
