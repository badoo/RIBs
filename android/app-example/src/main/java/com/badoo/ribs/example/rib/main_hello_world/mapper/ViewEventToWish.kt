package com.badoo.ribs.example.rib.main_hello_world.mapper

import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldView
import com.badoo.ribs.example.rib.main_hello_world.feature.MainHelloWorldFeature

internal object ViewEventToWish : (MainHelloWorldView.Event) -> MainHelloWorldFeature.Wish? {

    override fun invoke(event: MainHelloWorldView.Event): MainHelloWorldFeature.Wish? =
        TODO("Implement HelloWorldViewEventToWish mapping")
}
