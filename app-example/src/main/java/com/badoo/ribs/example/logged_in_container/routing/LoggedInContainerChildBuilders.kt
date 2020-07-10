package com.badoo.ribs.example.logged_in_container.routing

import com.badoo.ribs.example.feed_container.FeedContainer
import com.badoo.ribs.example.feed_container.FeedContainerBuilder
import com.badoo.ribs.example.logged_in_container.LoggedInContainer

internal class LoggedInContainerChildBuilders(
    dependency: LoggedInContainer.Dependency
) {
    private val subtreeDeps = SubtreeDeps(dependency)

    val photoFeedBuilder = FeedContainerBuilder(subtreeDeps)

    private class SubtreeDeps(dependency: LoggedInContainer.Dependency) :
        LoggedInContainer.Dependency by dependency, FeedContainer.Dependency
}
