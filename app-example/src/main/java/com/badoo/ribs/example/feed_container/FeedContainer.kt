package com.badoo.ribs.example.feed_container

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.feed_container.FeedContainer.Input
import com.badoo.ribs.example.feed_container.FeedContainer.Output
import com.badoo.ribs.example.feed_container.routing.FeedContainerRouter
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.routing.transition.handler.TransitionHandler

interface FeedContainer : Rib, Connectable<Input, Output> {

    interface Dependency {
        val authDataSource: AuthDataSource
        val api: UnsplashApi
    }

    sealed class Input

    sealed class Output {
        data class PhotoClicked(val id: String) : Output()
    }

    class Customisation(
        val viewFactory: FeedContainerView.Factory = FeedContainerViewImpl.Factory(),
        val transitionHandler: TransitionHandler<FeedContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
