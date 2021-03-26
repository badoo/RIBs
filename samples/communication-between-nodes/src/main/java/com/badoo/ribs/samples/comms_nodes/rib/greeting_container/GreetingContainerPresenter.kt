package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector

interface GreetingContainerPresenter

internal class GreetingContainerPresenterImpl(
    private val backStack: BackStack<GreetingContainerRouter.Configuration>
) : GreetingContainerPresenter,
    SubtreeChangeAware {

    private var greetingInput: Relay<Greeting.Input>? = null

    override fun onChildBuilt(child: Node<*>) {
        super.onChildBuilt(child)
        when (child) {
            is Greeting -> {
                greetingInput = child.input
                child.output.observe(::onGreetingOutput)
            }
            is LanguageSelector -> child.output.observe(::onLanguageSelectorOutput)
        }
    }

    private fun onGreetingOutput(output: Greeting.Output) {
        when (output) {
            is Greeting.Output.ChangeLanguage -> backStack.push(GreetingContainerRouter.Configuration.LanguageSelector(output.currentLanguage))
        }
    }

    private fun onLanguageSelectorOutput(output: LanguageSelector.Output) {
        when (output) {
            is LanguageSelector.Output.LanguageSelected -> {
                backStack.popBackStack()
                greetingInput?.accept(Greeting.Input.UpdateGreeting(output.language))
            }
        }
    }
}