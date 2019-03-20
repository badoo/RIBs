package com.badoo.ribs.example.rib.dialog_example

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class DialogExampleRouterTest {

    private var interactor: DialogExampleInteractor = mock()
    private var router: DialogExampleRouter? = null

    @Before
    fun setup() {
        router = DialogExampleRouter()
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
