package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class DialogLoremIpsumInteractorTest {

    private val router: DialogLoremIpsumRouter = mock()
    private val output: Consumer<DialogLoremIpsum.Output> = mock()
    private lateinit var interactor: DialogLoremIpsumInteractor

    @Before
    fun setup() {
        interactor = DialogLoremIpsumInteractor(
            savedInstanceState = null,
            router = router,
            output = output
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
