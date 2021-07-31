package com.badoo.ribs.example.photo_feed.view

import com.badoo.ribs.example.photo_feed.Photo

sealed class PhotoListItem(val id: String) {

    data class PhotoItem(val photo: Photo, val onClicked: (Photo) -> Unit) : PhotoListItem(photo.id)
    object NextPageLoadingItem : PhotoListItem("nextPageLoading")
    data class NextPageLoadingErrorItem(val onClicked: () -> Unit) :
        PhotoListItem("nextPageLoadingError")
}
