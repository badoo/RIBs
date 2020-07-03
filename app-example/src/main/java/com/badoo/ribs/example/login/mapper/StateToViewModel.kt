package com.badoo.ribs.example.login.mapper

import com.badoo.ribs.example.login.LoginView.ViewModel
import com.badoo.ribs.example.login.feature.LoginFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel()
}
