package com.badoo.ribs.samples.transitionanimations.rib.parent

import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration
import com.badoo.ribs.samples.transitionanimations.rib.parent.ParentView.Event

interface ParentPresenter {
    fun handle(event: Event)
}

internal class ParentPresenterImpl(
        private val backStack: BackStack<Configuration>
) : ParentPresenter {

    override fun handle(event: Event) {
        when (event) {
            is Event.PushNextChild -> backStack.push(backStack.activeConfiguration.toNextConfiguration())
        }
    }

    private fun Configuration.toNextConfiguration() =
            when (this) {
                is Configuration.Child1 -> Configuration.Child2
                is Configuration.Child2 -> Configuration.Child1
            }
}