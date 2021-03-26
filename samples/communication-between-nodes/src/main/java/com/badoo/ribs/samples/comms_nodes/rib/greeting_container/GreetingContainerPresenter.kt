package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting

interface GreetingContainerPresenter

internal class GreetingContainerPresenterImpl(
    private val backStack: BackStack<GreetingContainerRouter.Configuration>
) : GreetingContainerPresenter,
    SubtreeChangeAware {

    override fun onChildBuilt(child: Node<*>) {
        super.onChildBuilt(child)
        when (child) {
            is Greeting -> child.output.observe(::onGreetingOutput)
        }
    }

    private fun onGreetingOutput(output: Greeting.Output) {
        when (output) {
            is Greeting.Output.ChangeLanguage -> backStack.push(GreetingContainerRouter.Configuration.LanguageSelector)
        }
    }
}