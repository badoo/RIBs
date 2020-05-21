package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarInteractorTest {

    private val feature: FooBarFeature = mock()
    private val router: FooBarRouter = mock()
    private lateinit var interactor: FooBarInteractor

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            buildParams = BuildParams.Empty(),
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
