package com.badoo.ribs.example.rib.root

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class RootRouterTest {

    private var interactor: RootInteractor = mock()
    private var router: RootRouter? = null

    @Before
    fun setup() {
        router = RootRouter(
            savedInstanceState = null
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
