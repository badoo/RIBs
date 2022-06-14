package com.badoo.ribs.example.root

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.example.RxSchedulerExtension
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.auth.AuthState
import com.badoo.ribs.example.root.routing.RootRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.test.InteractorTestHelper
import com.badoo.ribs.test.emptyBuildParams
import com.jakewharton.rxrelay2.PublishRelay
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(RxSchedulerExtension::class)
class RootInteractorAuthStateTest {

    private val backStack: BackStack<Configuration> = mock()

    private val stateRelay = PublishRelay.create<AuthState>()
    private val authDataSource = mock<AuthDataSource>().apply {
        whenever(authUpdates).thenReturn(stateRelay)
    }
    private lateinit var interactor: RootInteractor
    private lateinit var interactorTestHelper: InteractorTestHelper<Nothing>

    @BeforeEach
    fun setup() {
        interactor = RootInteractor(
            buildParams = emptyBuildParams(),
            backStack = backStack,
            authDataSource = authDataSource,
            networkErrors = PublishRelay.create()
        )
        interactorTestHelper = InteractorTestHelper(interactor)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `an example test with some conditions should pass`(
        authState: AuthState,
        expectedConfiguration: Configuration
    ) {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.CREATED) {
            stateRelay.accept(authState)

            verify(backStack).replace(expectedConfiguration)
        }
    }

    companion object {
        @JvmStatic
        fun data(): Stream<Arguments> = Stream.of(
            Arguments.of(AuthState.Unauthenticated, Configuration.Content.LoggedOut),
            Arguments.of(AuthState.Anonymous, Configuration.Content.LoggedIn),
            Arguments.of(AuthState.Authenticated(""), Configuration.Content.LoggedIn)
        )
    }
}
