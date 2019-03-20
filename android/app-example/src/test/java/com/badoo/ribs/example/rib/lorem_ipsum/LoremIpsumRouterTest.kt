package com.badoo.ribs.example.rib.lorem_ipsum

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class LoremIpsumRouterTest {

    private var interactor: LoremIpsumInteractor = mock()
    private var router: LoremIpsumRouter? = null

    @Before
    fun setup() {
        router = LoremIpsumRouter()
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
