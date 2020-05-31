package com.badoo.ribs.example.auth

import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.AccessToken
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class AuthDataSourceImplTest {
    private lateinit var authDataSource: AuthDataSourceImpl
    private var api: UnsplashApi = mock()
    private var storage: AuthStateStorage = mock()
    private lateinit var authStateSubscriber: TestObserver<AuthState>

    @Before
    fun setUp() {
        whenever(storage.restore()).thenReturn(AuthState.Unauthenticated)

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
        whenever(storage.restore()).thenReturn(AuthState.Authenticated(token))

        authDataSource = AuthDataSourceImpl(
            api = api,
            storage = storage,
            accessKey = accessKey,
            clientSecret = clientSecret
        )

        assertThat(authDataSource.getState()).isEqualTo(AuthState.Authenticated(token))
    }

    @Test
    fun `when successful login then state updated to authenticated`() {
        val authCode = "code"
        val authResult = createAccessTokenRequest()
        mockLoginResponse(authCode, authResult)

        authDataSource.login(authCode).subscribe()

        authStateSubscriber.assertLastValueEqual(AuthState.Authenticated(authResult.accessToken))
    }

    @Test
    fun `when successful login then state is authenticated`() {
        val authCode = "code"
        val authResult = createAccessTokenRequest()
        mockLoginResponse(authCode, authResult)

        authDataSource.login(authCode).subscribe()

        assertThat(authDataSource.getState()).isEqualTo(AuthState.Authenticated(authResult.accessToken))
    }

    @Test
    fun `when successful login then state is saved to storage`() {
        val authCode = "code"
        val authResult = createAccessTokenRequest()
        mockLoginResponse(authCode, authResult)

        authDataSource.login(authCode).subscribe()

        verify(storage).save(AuthState.Authenticated(authResult.accessToken))
    }

    @Test
    fun `when login anonymous then state is anonymous`() {
        authDataSource.loginAnonymous()

        assertThat(authDataSource.getState()).isEqualTo(AuthState.Anonymous)
    }

    @Test
    fun `when login anonymous then state is saved to storage`() {
        authDataSource.loginAnonymous()

        verify(storage).save(AuthState.Anonymous)
    }

    @Test
    fun `when logout then state is anonymous`() {
        authDataSource.logout()

        assertThat(authDataSource.getState()).isEqualTo(AuthState.Unauthenticated)
    }

    @Test
    fun `when logout then state is saved to storage`() {
        authDataSource.loginAnonymous()

        verify(storage).save(AuthState.Anonymous)
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
