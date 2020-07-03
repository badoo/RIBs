package com.badoo.ribs.example.login

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.login.Login.Input
import com.badoo.ribs.example.login.Login.Output

interface Login : Rib, Connectable<Input, Output> {

    interface Dependency {
        val authCodeDataSource: AuthCodeDataSource
        val authDataSource: AuthDataSource
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: LoginView.Factory = LoginViewImpl.Factory()
    ) : RibCustomisation
}


