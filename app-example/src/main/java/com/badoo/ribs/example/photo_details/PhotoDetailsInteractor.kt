package com.badoo.ribs.example.photo_details

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.auth.AuthState.Authenticated
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.Wish
import com.badoo.ribs.example.photo_details.mapper.NewsToOutput
import com.badoo.ribs.example.photo_details.mapper.StateToViewModel
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration.Content
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import io.reactivex.functions.Consumer

internal class PhotoDetailsInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStackFeature<Configuration>,
    private val feature: PhotoDetailsFeature,
    private val authDataSource: AuthDataSource,
    private val portal: Portal.OtherSide
) : Interactor<PhotoDetails, PhotoDetailsView>(
    buildParams = buildParams,
    disposables = feature
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature.news to rib.output using NewsToOutput)
        }
    }

    override fun onViewCreated(view: PhotoDetailsView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to likeEventConsumer)
        }
    }

    private val likeEventConsumer = Consumer<PhotoDetailsView.Event> { event ->
        when (event) {
            is PhotoDetailsView.Event.LikeClicked -> {
                if (authDataSource.getState() is Authenticated) {
                    feature.accept(Wish.LikePhoto)
                } else {
                    portal.showContent(node, Content.Login)
                }
            }
        }

    }

    override fun onChildCreated(child: Node<*>) {
        /**
         * TODO bind children here and delete this comment block.
         *
         *  At this point children haven't set their own bindings yet,
         *  so it's safe to setup listening to their output before they start emitting.
         *
         *  On the other hand, they're not ready to receive inputs yet. Usually this is alright.
         *  If it's a requirement though, create those bindings in [onAttachChild]
         */
        // child.lifecycle.createDestroy {
        // when (child) {
        // is Child1 -> bind(child.output to someConsumer)
        // }
        // }
    }
}
