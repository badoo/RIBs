package com.badoo.ribs.example.rib.main_hello_world.mapper

import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorld
import com.badoo.ribs.example.rib.main_hello_world.feature.MainHelloWorldFeature

internal object InputToWish : (MainHelloWorld.Input) -> MainHelloWorldFeature.Wish? {

    override fun invoke(event: MainHelloWorld.Input): MainHelloWorldFeature.Wish? =
        TODO("Implement HelloWorldInputToWish mapping")
}
