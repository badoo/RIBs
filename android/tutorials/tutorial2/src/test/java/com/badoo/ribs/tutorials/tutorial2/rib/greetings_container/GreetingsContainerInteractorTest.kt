package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.feature.GreetingsContainerFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class GreetingsContainerInteractorTest {

    private val input: ObservableSource<GreetingsContainer.Input> = mock()
    private val output: Consumer<GreetingsContainer.Output> = mock()
    private val feature: GreetingsContainerFeature = mock()
    private val router: GreetingsContainerRouter = mock()
    private lateinit var interactor: GreetingsContainerInteractor

    @Before
    fun setup() {
        interactor = GreetingsContainerInteractor(
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
