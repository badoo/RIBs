package com.badoo.ribs.example.app_bar.mapper

import com.badoo.ribs.example.app_bar.AppBar.Output
import com.badoo.ribs.example.app_bar.AppBarView.Event

internal object ViewEventToOutput : (Event) -> Output? {

    override fun invoke(event: Event): Output? =
        when (event) {
            is Event.UserClicked -> Output.UserClicked
            is Event.SearchClicked -> Output.SearchClicked
        }
}
