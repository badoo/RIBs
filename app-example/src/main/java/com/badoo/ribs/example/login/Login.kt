package com.badoo.ribs.example.login

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.login.Login.Input
import com.badoo.ribs.example.login.Login.Output
import com.badoo.ribs.rx2.clienthelper.connector.Connectable

interface Login : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvideActivityStarter {
        val authCodeDataSource: AuthCodeDataSource
        val authDataSource: AuthDataSource
    }

    sealed class Input

    sealed class Output

    class Customisation : RibCustomisation
}


