package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.routing.GreetingContainerRouter
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector

internal class GreetingContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<GreetingContainerRouter.Configuration>
) : Interactor<GreetingContainer, Nothing>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        super.onCreate(nodeLifecycle)

        childAware(nodeLifecycle) {
            whenChildrenAttached<Greeting, LanguageSelector> { _, greeting, languageSelector ->
                languageSelector.output.observe {
                    greeting.input.accept(languageSelectorOutputToGreetingInput(it))
                }
            }
            whenChildBuilt<Greeting> { _, child -> child.output.observe(::onGreetingOutput) }
            whenChildBuilt<LanguageSelector> { _, child -> child.output.observe(::onLanguageSelectorOutput) }
        }
    }

    private val languageSelectorOutputToGreetingInput: (LanguageSelector.Output) -> Greeting.Input = {
        when (it) {
            is LanguageSelector.Output.LanguageSelected -> Greeting.Input.UpdateGreeting(it.selectedIndex)
        }
    }

    private fun onGreetingOutput(output: Greeting.Output) {
        when (output) {
            is Greeting.Output.AvailableLanguagesDisplayed -> backStack.push(GreetingContainerRouter.Configuration.ChooseLanguage(output.currentLanguage))
        }
    }

    private fun onLanguageSelectorOutput(output: LanguageSelector.Output) {
        when (output) {
            is LanguageSelector.Output.LanguageSelected -> backStack.popBackStack()
        }
    }
}
