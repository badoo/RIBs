package com.badoo.ribs.example.rib.main_dialog_example

import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainDialogExampleInteractorTest {

    private val router: MainDialogExampleRouter = mock()
    private lateinit var interactor: MainDialogExampleInteractor

    @Before
    fun setup() {
        interactor = MainDialogExampleInteractor(
            savedInstanceState = null,
            router = router,
            simpleDialog = mock(),
            lazyDialog = mock(),
            ribDialog = mock()
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
