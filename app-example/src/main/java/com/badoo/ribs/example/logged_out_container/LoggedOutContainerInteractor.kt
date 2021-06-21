package com.badoo.ribs.example.logged_out_container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.childaware.whenChildBuilt
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter.Configuration
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter.Configuration.Content
import com.badoo.ribs.example.welcome.Welcome
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import io.reactivex.functions.Consumer

internal class LoggedOutContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
    private val authDataSource: AuthDataSource
) : Interactor<LoggedOutContainer, Nothing>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
        }
        whenChildBuilt<Welcome>(nodeLifecycle) { commonLifecycle, child ->
            commonLifecycle.createDestroy {
                bind(child.output to welcomeListener)
            }
        }
    }

    private val welcomeListener = Consumer<Welcome.Output> { output ->
        when (output) {
            Welcome.Output.RegisterClicked -> backStack.push(Content.Register)
            Welcome.Output.LoginClicked -> backStack.push(Content.Login)
        }
    }
}

