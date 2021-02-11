package com.badoo.ribs.example.app_bar

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.app_bar.AppBar.Input
import com.badoo.ribs.example.app_bar.AppBar.Output
import com.badoo.ribs.example.repository.UserRepository

interface AppBar : Rib, Connectable<Input, Output> {

    interface Dependency {
        val userRepository: UserRepository
    }

    sealed class Input

    sealed class Output {
        object SearchClicked : Output()
        object UserClicked : Output()
    }

    class Customisation(
        val viewFactory: AppBarView.Factory = AppBarViewImpl.Factory()
    ) : RibCustomisation
}
