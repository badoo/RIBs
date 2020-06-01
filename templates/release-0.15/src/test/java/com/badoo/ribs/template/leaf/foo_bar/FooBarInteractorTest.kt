package com.badoo.ribs.template.leaf.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarInteractorTest {

    private val input: ObservableSource<FooBar.Input> = mock()
    private val output: Consumer<FooBar.Output> = mock()
    private val feature: FooBarFeature = mock()
    private lateinit var interactor: FooBarInteractor

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            buildParams = BuildParams.Empty(),
            input = input,
            output = output,
            feature = feature
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
