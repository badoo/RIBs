package com.badoo.ribs.example.logged_out_container

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer.Input
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer.Output
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter
import com.badoo.ribs.example.login.AuthCodeDataSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler

interface LoggedOutContainer : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvideActivityStarter {
        val authDataSource: AuthDataSource
        val authCodeDataSource: AuthCodeDataSource
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val transitionHandler: TransitionHandler<LoggedOutContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
