package com.badoo.ribs.template.leaf.foo_bar

import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature
import com.badoo.ribs.test.emptyBuildParams
import org.mockito.kotlin.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarInteractorTest {

    private val feature: FooBarFeature = mock()
    private lateinit var interactor: FooBarInteractor

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            buildParams = emptyBuildParams(),
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
