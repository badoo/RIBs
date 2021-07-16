package com.badoo.ribs.example.photo_feed

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature
import com.badoo.ribs.example.photo_feed.mapper.StateToViewModel
import com.badoo.ribs.example.photo_feed.mapper.ViewEventToOutput
import com.badoo.ribs.example.photo_feed.mapper.ViewEventToWish
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView

internal class PhotoFeedInteractor(
    buildParams: BuildParams<*>,
    private val feature: PhotoFeedFeature
) : Interactor<PhotoFeed, PhotoFeedView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: PhotoFeedView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
