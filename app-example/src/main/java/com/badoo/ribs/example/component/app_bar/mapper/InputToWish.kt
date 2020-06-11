package com.badoo.ribs.example.component.app_bar.mapper

import com.badoo.ribs.example.component.app_bar.AppBar.Input
import com.badoo.ribs.example.component.app_bar.feature.AppBarFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement AppBarInputToWish mapping")
}
