package com.badoo.ribs.example.welcome.mapper

import com.badoo.ribs.example.welcome.Welcome.Output
import com.badoo.ribs.example.welcome.WelcomeView


internal object ViewEventToOutput : (WelcomeView.Event) -> Output? {
    override fun invoke(event: WelcomeView.Event): Output? =
        when (event) {
            is WelcomeView.Event.LoginClicked -> Output.LoginClicked
            is WelcomeView.Event.RegisterClicked -> Output.RegisterClicked
            is WelcomeView.Event.SkipClicked -> null
        }
}
