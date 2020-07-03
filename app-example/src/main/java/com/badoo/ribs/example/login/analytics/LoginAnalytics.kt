package com.badoo.ribs.example.login.analytics

import com.badoo.ribs.example.login.LoginView
import io.reactivex.functions.Consumer

internal object LoginAnalytics : Consumer<LoginAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: LoginView.Event) : Event()
    }

    override fun accept(event: LoginAnalytics.Event) {
        // TODO Implement tracking
    }
}
