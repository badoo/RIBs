package com.badoo.ribs.example.logged_in_container

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_in_container.LoggedInContainer.Input
import com.badoo.ribs.example.logged_in_container.LoggedInContainer.Output
import com.badoo.ribs.example.logged_in_container.routing.LoggedInContainerRouter
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.routing.transition.handler.TransitionHandler

interface LoggedInContainer : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvidePortal {
        val authDataSource: AuthDataSource
        val api: UnsplashApi
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val transitionHandler: TransitionHandler<LoggedInContainerRouter.Configuration>? = null
    ) : RibCustomisation
}
