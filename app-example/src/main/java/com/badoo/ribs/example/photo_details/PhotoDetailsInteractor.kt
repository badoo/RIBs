package com.badoo.ribs.example.photo_details

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.News
import com.badoo.ribs.example.photo_details.mapper.NewsToOutput
import com.badoo.ribs.example.photo_details.mapper.StateToViewModel
import com.badoo.ribs.example.photo_details.mapper.ViewEventToWish
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration.Content
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import io.reactivex.functions.Consumer

internal class PhotoDetailsInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStackFeature<Configuration>,
    private val feature: PhotoDetailsFeature,
    private val portal: Portal.OtherSide
) : Interactor<PhotoDetails, PhotoDetailsView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
            bind(feature.news to newsConsumer)
        }
    }

    override fun onViewCreated(view: PhotoDetailsView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
        }
    }

    private val newsConsumer = Consumer<News> { news ->
        when (news) {
            is News.ShowLogin -> portal.showContent(node, Content.Login)
        }
    }
}
