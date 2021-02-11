package com.badoo.ribs.example.photo_feed

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.photo_feed.PhotoFeed.Input
import com.badoo.ribs.example.photo_feed.PhotoFeed.Output
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView
import com.badoo.ribs.example.photo_feed.view.PhotoFeedViewImpl

interface PhotoFeed : Rib, Connectable<Input, Output> {

    interface Dependency {
        val dataSource: PhotoFeedDataSource
    }

    sealed class Input

    sealed class Output {
        data class PhotoClicked(val photo: Photo): Output()
    }

    class Customisation(
        val viewFactory: PhotoFeedView.Factory = PhotoFeedViewImpl.Factory()
    ) : RibCustomisation
}
