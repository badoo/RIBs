package com.badoo.ribs.example.feed_container

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.example.R
import com.badoo.ribs.example.photo_feed.PhotoFeed

interface FeedContainerView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, FeedContainerView>
}


class FeedContainerViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    FeedContainerView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_feed_container
    ) : FeedContainerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<FeedContainerView> = ViewFactory {
            FeedContainerViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val photoFeedContainer = findViewById<ViewGroup>(R.id.photoFeedContainer)

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is PhotoFeed -> photoFeedContainer
            else -> super.getParentViewForSubtree(subtreeOf)
        }
}
