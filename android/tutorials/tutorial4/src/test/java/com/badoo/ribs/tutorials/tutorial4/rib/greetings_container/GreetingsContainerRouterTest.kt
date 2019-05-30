package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class GreetingsContainerRouterTest {

    private var interactor: GreetingsContainerInteractor = mock()
    private var router: GreetingsContainerRouter? = null

    @Before
    fun setup() {
        router = GreetingsContainerRouter()
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
