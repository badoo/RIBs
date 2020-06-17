package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.compose_leaf.feature.ComposeLeafFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class ComposeLeafInteractorTest {

    private val feature: ComposeLeafFeature = mock()
    private lateinit var interactor: ComposeLeafInteractor

    @Before
    fun setup() {
        interactor = ComposeLeafInteractor(
            buildParams = BuildParams.Empty(),
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
