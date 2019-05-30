package com.badoo.ribs.tutorials.tutorial4.rib.hello_world

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class HelloWorldRouterTest {

    private var interactor: HelloWorldInteractor = mock()
    private var router: HelloWorldRouter? = null

    @Before
    fun setup() {
        router = HelloWorldRouter()
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
