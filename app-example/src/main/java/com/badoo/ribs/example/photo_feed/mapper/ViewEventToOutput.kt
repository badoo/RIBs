package com.badoo.ribs.example.photo_feed.mapper

import com.badoo.ribs.example.photo_feed.PhotoFeed.Output
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.Event

internal object ViewEventToOutput : (Event) -> Output? {
    override fun invoke(event: Event): Output? =
        when (event) {
            is Event.PhotoClicked -> Output.PhotoClicked(event.photo)
            else -> null
        }

}
