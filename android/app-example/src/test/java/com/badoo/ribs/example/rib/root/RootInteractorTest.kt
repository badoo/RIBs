package com.badoo.ribs.example.rib.root

import com.badoo.ribs.example.rib.root.feature.RootFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class RootInteractorTest {

    private val input: ObservableSource<Root.Input> = mock()
    private val output: Consumer<Root.Output> = mock()
    private val feature: RootFeature = mock()
    private val router: RootRouter = mock()
    private lateinit var interactor: RootInteractor

    @Before
    fun setup() {
        interactor = RootInteractor(
            savedInstanceState = null,
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
