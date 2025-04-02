package com.badoo.ribs.sandbox.rib.foo_bar

import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.test.emptyBuildParams
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class FooBarInteractorTest {

    private val permissionRequester: PermissionRequester = mock()
    private lateinit var interactor: FooBarInteractor

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            buildParams = emptyBuildParams(),
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
