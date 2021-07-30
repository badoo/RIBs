package com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting.Greeting.Input
import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting.Greeting.Input.UpdateGreeting
import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting.Greeting.Output.LanguageChangeRequested
import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting.GreetingView.ViewModel
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.Language
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.Language.ENGLISH
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.Language.FRENCH
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.Language.GERMAN

interface GreetingPresenter {
    fun onChangeLanguageClicked()
}

internal class GreetingPresenterImpl(
    ribAware: RibAware<Greeting> = RibAwareImpl(),
    defaultLanguage: Language = ENGLISH
) : GreetingPresenter,
    NodeLifecycleAware,
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

    override fun onCreate(nodeLifecycle: Lifecycle) {
        super.onCreate(nodeLifecycle)
        nodeLifecycle.subscribe(
            onCreate = {
                cancellables += rib.input.observe(::onGreetingInput)
            },
            onDestroy = {
                cancellables.cancel()
            }
        )
    }

    private fun onGreetingInput(input: Input) {
        when (input) {
            is UpdateGreeting -> {
                currentLanguage = Language.values()[input.selectedIndex]
            }
        }
    }

    override fun onViewCreated(view: GreetingView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.subscribe(
            onCreate = {
                this@GreetingPresenterImpl.view = view
                view.accept(buildViewModel())
            },
            onDestroy = {
                this@GreetingPresenterImpl.view = null
            }
        )
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
        rib.output.accept(LanguageChangeRequested(currentLanguage))
    }
}
