package com.badoo.ribs.example.rib.dialog_example

import com.badoo.ribs.example.rib.dialog_example.feature.DialogExampleFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class DialogExampleInteractorTest {

    private val input: ObservableSource<DialogExample.Input> = mock()
    private val output: Consumer<DialogExample.Output> = mock()
    private val feature: DialogExampleFeature = mock()
    private val router: DialogExampleRouter = mock()
    private lateinit var interactor: DialogExampleInteractor

    @Before
    fun setup() {
        interactor = DialogExampleInteractor(
            input = input,
            output = output,
            feature = feature,
            router = router
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
