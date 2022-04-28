package com.badoo.ribs.example.welcome

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.welcome.Welcome.Input
import com.badoo.ribs.example.welcome.Welcome.Output

interface Welcome : Rib, Connectable<Input, Output> {

    interface Dependency {
        val authDataSource: AuthDataSource
    }

    sealed class Input

    sealed class Output {
        object RegisterClicked : Output()
        object LoginClicked : Output()
    }

    class Customisation(
        val viewFactory: WelcomeView.Factory = WelcomeViewImpl.Factory()
    ) : RibCustomisation
}
