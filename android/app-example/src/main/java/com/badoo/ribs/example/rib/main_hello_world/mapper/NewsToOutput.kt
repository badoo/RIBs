package com.badoo.ribs.example.rib.main_hello_world.mapper

import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorld
import com.badoo.ribs.example.rib.main_hello_world.feature.MainHelloWorldFeature

internal object NewsToOutput : (MainHelloWorldFeature.News) -> MainHelloWorld.Output? {

    override fun invoke(news: MainHelloWorldFeature.News): MainHelloWorld.Output? =
        TODO("Implement HelloWorldNewsToOutput mapping")
}
