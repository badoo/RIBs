package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.test.emptyBuildParams
import org.mockito.kotlin.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class DialogExampleInteractorTest {

    private lateinit var interactor: DialogExampleInteractor

    @Before
    fun setup() {
        interactor = DialogExampleInteractor(
            buildParams = emptyBuildParams(),
            backStack = mock(),
            dialogs = mock()
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
