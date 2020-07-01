package com.badoo.ribs.example.component.app_bar.analytics

import com.badoo.ribs.example.component.app_bar.AppBarView
import io.reactivex.functions.Consumer

internal object AppBarAnalytics : Consumer<AppBarAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: AppBarView.Event) : Event()
    }

    override fun accept(event: AppBarAnalytics.Event) {
        // TODO Implement tracking
    }
}
