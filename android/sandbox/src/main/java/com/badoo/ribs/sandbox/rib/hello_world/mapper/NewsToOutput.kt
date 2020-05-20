package com.badoo.ribs.sandbox.rib.hello_world.mapper

import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.sandbox.rib.hello_world.feature.HelloWorldFeature

internal object NewsToOutput : (HelloWorldFeature.News) -> Output? {

    override fun invoke(news: HelloWorldFeature.News): Output? =
        TODO("Implement HelloWorldNewsToOutput mapping")
}
