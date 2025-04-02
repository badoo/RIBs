package com.badoo.ribs.example.app_bar

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.app_bar.AppBar.Input
import com.badoo.ribs.example.app_bar.AppBar.Output
import com.badoo.ribs.example.repository.UserRepository
import com.badoo.ribs.rx3.clienthelper.connector.Connectable

interface AppBar : Rib, Connectable<Input, Output> {

    interface Dependency {
        val userRepository: UserRepository
    }

    sealed class Input

    sealed class Output {
        data object SearchClicked : Output()
        data object UserClicked : Output()
    }

    class Customisation(
        val viewFactory: AppBarView.Factory = AppBarViewImpl.Factory()
    ) : RibCustomisation
}
