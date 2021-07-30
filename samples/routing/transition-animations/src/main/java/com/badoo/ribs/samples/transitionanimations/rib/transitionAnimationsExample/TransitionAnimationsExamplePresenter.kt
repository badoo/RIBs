package com.badoo.ribs.samples.transitionanimations.rib.transitionAnimationsExample

import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.transitionanimations.rib.transitionAnimationsExample.routing.TransitionAnimationsExampleRouter.Configuration

interface TransitionAnimationsExamplePresenter {
    fun goToNext()
}

internal class TransitionAnimationsExamplePresenterImpl(
    private val backStack: BackStack<Configuration>
) : TransitionAnimationsExamplePresenter {

    override fun goToNext() {
        backStack.push(backStack.activeConfiguration.toNextConfiguration())
    }

    private fun Configuration.toNextConfiguration() =
        when (this) {
            is Configuration.Child1 -> Configuration.Child2
            is Configuration.Child2 -> Configuration.Child3
            is Configuration.Child3 -> Configuration.Child1
        }
}
