package com.badoo.ribs.example.rib.lorem_ipsum.mapper

import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum.Output
import com.badoo.ribs.example.rib.lorem_ipsum.feature.LoremIpsumFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        TODO("Implement LoremIpsumNewsToOutput mapping")
}
