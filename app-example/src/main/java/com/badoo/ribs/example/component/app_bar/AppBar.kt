package com.badoo.ribs.example.component.app_bar

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.component.app_bar.AppBar.Input
import com.badoo.ribs.example.component.app_bar.AppBar.Output
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter
import com.badoo.ribs.example.repository.UserRepository
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import io.reactivex.Single

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
        val viewFactory: AppBarView.Factory = AppBarViewImpl.Factory(),
        val transitionHandler: TransitionHandler<AppBarRouter.Configuration>? = null
    ) : RibCustomisation

    // Workflow
    // todo: do not delete - rename, and add more
    // todo: expose all meaningful operations
    fun businessLogicOperation(): Single<AppBar>

    // todo: expose all possible children (even permanent parts), or remove if there's none
    // fun attachChild1(): Single<Child>
}
