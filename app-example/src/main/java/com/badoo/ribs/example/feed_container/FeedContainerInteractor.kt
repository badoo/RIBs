package com.badoo.ribs.example.feed_container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.feed_container.mapper.FeedOutputToContainerOutput
import com.badoo.ribs.example.photo_feed.PhotoFeed

internal class FeedContainerInteractor(
    buildParams: BuildParams<*>
) : Interactor<FeedContainer, FeedContainerView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
        }
    }

    override fun onViewCreated(view: FeedContainerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
        }
    }

    override fun onChildBuilt(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is PhotoFeed -> bind(child.output to rib.output using FeedOutputToContainerOutput)
            }
        }
    }
}
