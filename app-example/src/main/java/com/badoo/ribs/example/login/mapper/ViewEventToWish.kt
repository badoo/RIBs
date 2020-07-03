package com.badoo.ribs.example.login.mapper

import com.badoo.ribs.example.login.LoginView.Event
import com.badoo.ribs.example.login.feature.LoginFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        TODO("Implement LoginViewEventToWish mapping")
}
