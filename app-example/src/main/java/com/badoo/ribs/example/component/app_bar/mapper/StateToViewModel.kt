package com.badoo.ribs.example.component.app_bar.mapper

import com.badoo.ribs.example.component.app_bar.AppBarView.ViewModel
import com.badoo.ribs.example.component.app_bar.feature.AppBarFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel =
        TODO("Implement StateToViewModel mapping")
}
