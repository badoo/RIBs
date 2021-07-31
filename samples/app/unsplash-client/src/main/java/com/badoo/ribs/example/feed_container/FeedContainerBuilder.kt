package com.badoo.ribs.example.feed_container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.feed_container.routing.FeedContainerChildBuilders
import com.badoo.ribs.example.feed_container.routing.FeedContainerRouter

class FeedContainerBuilder(
    private val dependency: FeedContainer.Dependency
) : SimpleBuilder<FeedContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): FeedContainer {
        val connections = FeedContainerChildBuilders(dependency)
        val customisation = buildParams.getOrDefault(FeedContainer.Customisation())
        val interactor = FeedContainerInteractor(
            buildParams = buildParams
        )
        val router = FeedContainerRouter(
            buildParams = buildParams,
            builders = connections,
            transitionHandler = customisation.transitionHandler
        )

        return FeedContainerNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor, router)
        )
    }

}
