package com.badoo.ribs.samples.comms_nodes.rib.greeting

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.samples.comms_nodes.app.Language

interface GreetingPresenter {
    fun onEvent(event: GreetingView.Event)
}

internal class GreetingPresenterImpl(
    ribAware: RibAware<Greeting> = RibAwareImpl(),
    defaultLanguage: Language = Language.English
) : GreetingPresenter,
    ViewAware<GreetingView>,
    RibAware<Greeting> by ribAware {

    private var view: GreetingView? = null
    private var cancellables = CompositeCancellable()

    private val greetingForLanguage = mapOf(
        Language.English to "Hello!",
        Language.German to "Guten Tag!",
        Language.French to "Bonjour"
    )

    private val initialViewModel = buildViewModel(defaultLanguage)

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
        view.accept(initialViewModel)
    }

    private fun onGreetingInput(input: Greeting.Input) {
        when (input) {
            is Greeting.Input.UpdateGreeting -> view?.accept(buildViewModel(input.language))
        }
    }

    private fun buildViewModel(language: Language): GreetingView.ViewModel =
        GreetingView.ViewModel(Text.Plain(greetingForLanguage[language] ?: throw IllegalStateException("no such language")))

    override fun onEvent(event: GreetingView.Event) {
        rib.output.accept(Greeting.Output.ChangeLanguage)
    }
}