package com.badoo.ribs.example.root

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.auth.AuthState
import com.badoo.ribs.example.network.NetworkError
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.example.root.routing.RootRouter.Configuration.Content.LoggedIn
import com.badoo.ribs.example.root.routing.RootRouter.Configuration.Content.LoggedOut
import com.badoo.ribs.example.root.routing.RootRouter.Configuration.Content.Login
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.replace
import io.reactivex.Observable
import io.reactivex.functions.Consumer

internal class RootInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
    private val authDataSource: AuthDataSource,
    private val networkErrors: Observable<NetworkError>
) : Interactor<Root, Nothing>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(authDataSource.authUpdates to authStateConsumer)
            bind(networkErrors to networkErrorsConsumer)
        }
    }

    private val authStateConsumer = Consumer<AuthState> { state ->
        popLoginIfNeeded()
        when (state) {
            is AuthState.Unauthenticated -> backStack.replace(LoggedOut)
            is AuthState.Anonymous, is AuthState.Authenticated -> backStack.replace(LoggedIn)
        }
    }

    private fun popLoginIfNeeded() {
        if (backStack.activeConfiguration is Login) {
            backStack.popBackStack()
        }
    }

    private val networkErrorsConsumer = Consumer<NetworkError> { error ->
        when (error) {
            is NetworkError.Unauthorized -> backStack.push(Login)
        }
    }
}
