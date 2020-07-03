package com.badoo.ribs.example.login

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.login.feature.LoginFeature
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginInteractorTest {

    private val feature: LoginFeature = mock()
    private lateinit var interactor: LoginInteractor

    @Before
    fun setup() {
        interactor = LoginInteractor(
            buildParams = BuildParams.Empty(),
            feature = feature
        )
    }

    @After
    fun tearDown() {
    }

    /**
     * TODO: Add real tests.
     */
    @Test
    fun `an example test with some conditions should pass`() {
        throw RuntimeException("Add real tests.")
    }
}
