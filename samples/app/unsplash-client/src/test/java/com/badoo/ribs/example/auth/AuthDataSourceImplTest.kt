package com.badoo.ribs.example.auth

import com.badoo.ribs.example.RxSchedulerRule
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.AccessToken
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthDataSourceImplTest {
    private lateinit var authDataSource: AuthDataSourceImpl
    private lateinit var storage: AuthStateStorage
    private var api: UnsplashApi = mock()
    private var persistence: AuthStatePersistence = mock()
    private lateinit var authStateSubscriber: TestObserver<AuthState>

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @Before
    fun setUp() {
        whenever(persistence.restore()).thenReturn(AuthState.Unauthenticated)

        storage = AuthStateStorageImpl(persistence)
        authDataSource = AuthDataSourceImpl(
            api = api,
            storage = storage,
            accessKey = accessKey,
            clientSecret = clientSecret
        )

        authStateSubscriber = TestObserver()
        authDataSource.authUpdates.subscribe(authStateSubscriber)
    }

    @Test
    fun `when init then state restored from storage`() {
        val token = "accessToken"
        whenever(persistence.restore()).thenReturn(AuthState.Authenticated(token))
        storage = AuthStateStorageImpl(persistence)
        authDataSource = AuthDataSourceImpl(
            api = api,
            storage = storage,
            accessKey = accessKey,
            clientSecret = clientSecret
        )

        assertThat(authDataSource.state).isEqualTo(AuthState.Authenticated(token))
    }

    @Test
    fun `when successful login then state updated to authenticated`() {
        val authCode = "code"
        val authResult = createAccessTokenRequest()
        mockLoginResponse(authCode, authResult)

        authDataSource.login(authCode).subscribe()

        authStateSubscriber.assertLastValueEqual(AuthState.Authenticated(authResult.access_token))
    }

    @Test
    fun `when successful login then state is authenticated`() {
        val authCode = "code"
        val authResult = createAccessTokenRequest()
        mockLoginResponse(authCode, authResult)

        authDataSource.login(authCode).subscribe()

        assertThat(authDataSource.state).isEqualTo(AuthState.Authenticated(authResult.access_token))
    }

    @Test
    fun `when successful login then state is saved to storage`() {
        val authCode = "code"
        val authResult = createAccessTokenRequest()
        mockLoginResponse(authCode, authResult)

        authDataSource.login(authCode).subscribe()

        assertThat(storage.state).isEqualTo(AuthState.Authenticated(authResult.access_token))
    }

    @Test
    fun `when login anonymous then state is anonymous`() {
        authDataSource.loginAnonymous()

        assertThat(authDataSource.state).isEqualTo(AuthState.Anonymous)
    }

    @Test
    fun `when login anonymous then state is saved to storage`() {
        authDataSource.loginAnonymous()

        assertThat(storage.state).isEqualTo(AuthState.Anonymous)
    }

    @Test
    fun `when logout then state is anonymous`() {
        authDataSource.logout()

        assertThat(authDataSource.state).isEqualTo(AuthState.Unauthenticated)
    }

    @Test
    fun `when logout then state is saved to storage`() {
        authDataSource.loginAnonymous()

        assertThat(storage.state).isEqualTo(AuthState.Anonymous)
    }

    private fun createAccessTokenRequest() = AccessToken("access token", "", "", "")
    private fun mockLoginResponse(
        authCode: String,
        authResult: AccessToken
    ) {
        whenever(
            api.requestAccessToken(
                clientId = accessKey,
                clientSecret = clientSecret,
                redirectUri = AuthConfig.redirectUri,
                code = authCode
            )
        ).thenReturn(
            Single.just(authResult)
        )
    }

    companion object {
        private const val accessKey = "key"
        private const val clientSecret = "secret"
    }
}


fun <T> TestObserver<T>.assertLastValueEqual(value: T) {
    assertThat(this.valueCount()).isGreaterThan(0)
    this.assertValueAt(valueCount() - 1, value)
}
