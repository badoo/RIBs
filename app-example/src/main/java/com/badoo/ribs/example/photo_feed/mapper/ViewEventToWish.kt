package com.badoo.ribs.example.photo_feed.mapper

import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.Wish
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.Event

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        when (event) {
            is Event.ScrolledToTheEnd,
            is Event.RetryInitialLoadingClicked,
            is Event.RetryNextPageLoadingClicked -> Wish.LoadNextPage
            else -> null
        }
}
