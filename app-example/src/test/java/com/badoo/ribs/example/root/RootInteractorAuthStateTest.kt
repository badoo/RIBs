package com.badoo.ribs.example.root

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.test.InteractorTestHelper
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.auth.AuthState
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RootInteractorAuthStateTest(
    private val authState: AuthState,
    private val expectedConfiguration: Configuration
) {
    private val backStack: BackStack<Configuration> = mock()
    private val stateRelay = PublishRelay.create<AuthState>()
    private val authDataSource = mock<AuthDataSource>().apply {
        whenever(authUpdates).thenReturn(stateRelay)
    }
    private lateinit var interactor: RootInteractor
    private lateinit var interactorTestHelper: InteractorTestHelper<Nothing>

    @Before
    fun setup() {
        interactor = RootInteractor(
            buildParams = BuildParams.Empty(),
            backStack = backStack,
            authDataSource = authDataSource
        )
        interactorTestHelper = InteractorTestHelper(interactor)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(AuthState.Unauthenticated, Configuration.Content.LoggedOut),
            arrayOf(AuthState.Anonymous, Configuration.Content.LoggedIn),
            arrayOf(AuthState.Authenticated(""), Configuration.Content.LoggedIn)
        )
    }

    @Test
    fun `an example test with some conditions should pass`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.CREATED) {
            stateRelay.accept(authState)

            verify(backStack).replace(expectedConfiguration)
        }
    }
}
