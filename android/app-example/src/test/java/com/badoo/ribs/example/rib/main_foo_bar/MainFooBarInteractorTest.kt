package com.badoo.ribs.example.rib.main_foo_bar

import com.badoo.ribs.android.PermissionRequester
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainFooBarInteractorTest {

    private val router: MainFooBarRouter = mock()
    private val permissionRequester: PermissionRequester = mock()
    private lateinit var interactor: MainFooBarInteractor

    @Before
    fun setup() {
        interactor = MainFooBarInteractor(
            savedInstanceState = null,
            router = router,
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
