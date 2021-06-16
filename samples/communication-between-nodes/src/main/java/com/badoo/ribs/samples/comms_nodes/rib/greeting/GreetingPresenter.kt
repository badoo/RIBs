package com.badoo.ribs.samples.comms_nodes.rib.greeting

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting.Input
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting.Input.UpdateGreeting
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting.Output.AvailableLanguagesDisplayed
import com.badoo.ribs.samples.comms_nodes.rib.greeting.GreetingView.ViewModel
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language.*

interface GreetingPresenter {
    fun onChangeLanguageClicked()
}

internal class GreetingPresenterImpl(
    ribAware: RibAware<Greeting> = RibAwareImpl(),
    defaultLanguage: Language = ENGLISH
) : GreetingPresenter,
    ViewAware<GreetingView>,
    RibAware<Greeting> by ribAware {

    private var view: GreetingView? = null
    private var cancellables = CompositeCancellable()
    private var currentLanguage: Language = defaultLanguage

    private val greetingForLanguage = mapOf(
        ENGLISH to "Hello!",
        GERMAN to "Guten Tag!",
        FRENCH to "Bonjour"
    )

    override fun onViewCreated(view: GreetingView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.subscribe(
            onCreate = {
                this@GreetingPresenterImpl.view = view
                cancellables += rib.input.observe(::onGreetingInput)
            },
            onDestroy = {
                this@GreetingPresenterImpl.view = null
                cancellables.cancel()
            }
        )
        val initialViewModel = buildViewModel()
        view.accept(initialViewModel)
    }

    private fun onGreetingInput(input: Input) {
        when (input) {
            is UpdateGreeting -> {
                currentLanguage = Language.values()[input.selectedIndex]
                view?.accept(buildViewModel())
            }
        }
    }

    private fun buildViewModel(): ViewModel {
        return ViewModel(
            Text.Plain(
                greetingForLanguage[currentLanguage]
                    ?: throw IllegalStateException("no such language")
            )
        )
    }

    override fun onChangeLanguageClicked() {
        rib.output.accept(AvailableLanguagesDisplayed(currentLanguage))
    }
}
