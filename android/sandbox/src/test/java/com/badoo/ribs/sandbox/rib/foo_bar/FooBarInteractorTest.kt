package com.badoo.ribs.sandbox.rib.foo_bar

import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.builder.BuildParams
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarInteractorTest {

    private val permissionRequester: PermissionRequester = mock()
    private lateinit var interactor: FooBarInteractor

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            buildParams = BuildParams.Empty(),
            permissionRequester = permissionRequester
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
    }
}
