package com.badoo.ribs.example.welcome

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.welcome.mapper.ViewEventToOutput
import io.reactivex.functions.Consumer

internal class WelcomeInteractor(
    buildParams: BuildParams<*>,
    authDataSource: AuthDataSource
) : Interactor<Welcome, WelcomeView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: WelcomeView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
            bind(view to rib.output using ViewEventToOutput)
        }
    }


    private val viewEventConsumer = Consumer<WelcomeView.Event> { event ->
        when (event) {
            is WelcomeView.Event.SkipClicked -> authDataSource.loginAnonymous()
            else -> {}
        }
    }
}
