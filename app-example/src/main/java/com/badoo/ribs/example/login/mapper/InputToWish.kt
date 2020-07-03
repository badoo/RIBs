package com.badoo.ribs.example.login.mapper

import com.badoo.ribs.example.login.Login.Input
import com.badoo.ribs.example.login.feature.LoginFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement LoginInputToWish mapping")
}
