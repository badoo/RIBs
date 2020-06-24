package com.badoo.ribs.example.logged_out_container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_out_container.routing.LoggedOutContainerRouter.Configuration
import com.badoo.ribs.example.welcome.Welcome
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.source.backstack.operation.replace
import io.reactivex.functions.Consumer

internal class LoggedOutContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStackFeature<Configuration>,
    private val authDataSource: AuthDataSource
) : Interactor<LoggedOutContainer, Nothing>(
    buildParams = buildParams
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
        }
    }

    override fun onChildCreated(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is Welcome -> {
                    bind(child.output to welcomeListener)
                }
            }
        }
    }

    private val welcomeListener = Consumer<Welcome.Output> { output ->
        when (output) {
            Welcome.Output.RegisterClicked -> backStack.replace(Configuration.Content.Register)
            Welcome.Output.LoginClicked -> backStack.replace(Configuration.Content.Login)
        }
    }
}

