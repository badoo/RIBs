package com.badoo.ribs.example.welcome

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.example.R
import com.badoo.ribs.example.welcome.WelcomeView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface WelcomeView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object LoginClicked : Event()
        object RegisterClicked : Event()
        object SkipClicked : Event()
    }

    interface Factory : ViewFactoryBuilder<Nothing?, WelcomeView>
}


class WelcomeViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    WelcomeView,
    ObservableSource<Event> by events {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_welcome
    ) : WelcomeView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<WelcomeView> = ViewFactory {
            WelcomeViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val skipAuth = findViewById<Button>(R.id.welcome_skip)
    private val login = findViewById<Button>(R.id.welcome_login)
    private val register = findViewById<Button>(R.id.welcome_register)

    init {
        skipAuth.setOnClickListener {
            events.accept(Event.SkipClicked)
        }
        login.setOnClickListener {
            events.accept(Event.LoginClicked)
        }
        register.setOnClickListener {
            events.accept(Event.RegisterClicked)
        }
    }
}
