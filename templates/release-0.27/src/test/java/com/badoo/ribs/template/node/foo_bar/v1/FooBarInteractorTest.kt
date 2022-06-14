package com.badoo.ribs.template.node.foo_bar.v1

import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.template.node.foo_bar.common.feature.FooBarFeature
import com.badoo.ribs.template.node.foo_bar.v1.FooBarInteractor
import com.badoo.ribs.template.node.foo_bar.v1.routing.FooBarRouter.Configuration
import com.badoo.ribs.test.emptyBuildParams
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarInteractorTest {

    private val feature: FooBarFeature = mock()
    private val backStack: BackStack<Configuration> = mock()
    private lateinit var interactor: FooBarInteractor

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            buildParams = emptyBuildParams(),
            feature = feature,
            backStack = backStack
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
