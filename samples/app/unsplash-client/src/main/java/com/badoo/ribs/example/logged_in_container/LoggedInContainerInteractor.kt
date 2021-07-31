package com.badoo.ribs.example.logged_in_container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.childaware.whenChildBuilt
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.feed_container.FeedContainer
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter.Configuration.Content
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.routing.source.backstack.BackStack
import io.reactivex.functions.Consumer

internal class LoggedInContainerInteractor(
    buildParams: BuildParams<*>,
    portal: Portal.OtherSide,
    private val backStack: BackStack<Configuration>
) : Interactor<LoggedInContainer, Nothing>(
    buildParams = buildParams
) {

    private val photoFeedOutputConsumer = Consumer<FeedContainer.Output> { output ->
        when (output) {
            is FeedContainer.Output.PhotoClicked -> portal.showContent(
                node,
                Content.PhotoDetails(output.id)
            )
        }
    }

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
        }
        whenChildBuilt<FeedContainer>(nodeLifecycle) { commonLifecycle, child ->
            commonLifecycle.createDestroy {
                bind(child.output to photoFeedOutputConsumer)
            }
        }
    }


}
