package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.feature.HelloWorldFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class HelloWorldInteractorTest {

    private val input: ObservableSource<HelloWorld.Input> = mock()
    private val output: Consumer<HelloWorld.Output> = mock()
    private val feature: HelloWorldFeature = mock()
    private val router: HelloWorldRouter = mock()
    private lateinit var interactor: HelloWorldInteractor

    @Before
    fun setup() {
        interactor = HelloWorldInteractor(
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
