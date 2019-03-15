package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.example.rib.lorem_ipsum.feature.LoremIpsumFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoremIpsumInteractorTest {

    private val input: ObservableSource<LoremIpsum.Input> = mock()
    private val output: Consumer<LoremIpsum.Output> = mock()
    private val feature: LoremIpsumFeature = mock()
    private val router: LoremIpsumRouter = mock()
    private lateinit var interactor: LoremIpsumInteractor

    @Before
    fun setup() {
        interactor = LoremIpsumInteractor(
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
