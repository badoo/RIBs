package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class DialogExampleInteractorTest {

    private lateinit var interactor: DialogExampleInteractor

    @Before
    fun setup() {
        interactor = DialogExampleInteractor(
            buildParams = BuildParams.Empty(),
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
