package com.badoo.ribs.example.photo_details.mapper

import com.badoo.ribs.example.photo_details.PhotoDetailsView.Event
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        when (event) {
            is Event.LikeClicked -> Wish.LikeOrUnlikePhoto
        }
}
