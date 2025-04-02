package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting.Greeting
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter.Configuration.ChooseLanguage
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.LanguageSelector

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
            whenChildAttached<Greeting> { _, greeting ->
                greeting.output.observe(::onGreetingOutput)
            }
            whenChildAttached<LanguageSelector> { _, languageSelector ->
                languageSelector.output.observe(::onLanguageSelectorOutput)
            }
        }
    }

    private fun onGreetingOutput(output: Greeting.Output) {
        when (output) {
            is Greeting.Output.LanguageChangeRequested -> backStack.push(ChooseLanguage(output.currentLanguage))
        }
    }

    private val languageSelectorOutputToGreetingInput: (LanguageSelector.Output) -> Greeting.Input =
        {
            when (it) {
                is LanguageSelector.Output.LanguageSelected -> Greeting.Input.UpdateGreeting(it.selectedIndex)
            }
        }

    private fun onLanguageSelectorOutput(output: LanguageSelector.Output) {
        when (output) {
            is LanguageSelector.Output.LanguageSelected -> backStack.popBackStack()
        }
    }
}
