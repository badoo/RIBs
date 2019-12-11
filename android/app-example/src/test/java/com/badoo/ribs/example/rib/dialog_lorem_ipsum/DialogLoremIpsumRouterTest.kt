package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class DialogLoremIpsumRouterTest {

    private var interactor: DialogLoremIpsumInteractor = mock()
    private var router: DialogLoremIpsumRouter? = null

    @Before
    fun setup() {
        router = DialogLoremIpsumRouter(
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
    }
}
