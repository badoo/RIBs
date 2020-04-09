package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.builder.BuildParams
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoremIpsumInteractorTest {

    private val output: Consumer<LoremIpsum.Output> = mock()
    private lateinit var interactor: LoremIpsumInteractor

    @Before
    fun setup() {
        interactor = LoremIpsumInteractor(
            buildParams = BuildParams.Empty(),
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
