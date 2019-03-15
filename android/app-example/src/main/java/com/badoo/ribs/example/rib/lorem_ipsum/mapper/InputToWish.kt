package com.badoo.ribs.example.rib.lorem_ipsum.mapper

import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum.Input
import com.badoo.ribs.example.rib.lorem_ipsum.feature.LoremIpsumFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement LoremIpsumInputToWish mapping")
}
