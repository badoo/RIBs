package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter
import com.badoo.ribs.test.emptyBuildParams
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FooBarRouterTest {

    private var router: FooBarRouter? = null

    @Before
    fun setup() {
        router = FooBarRouter(
            buildParams = emptyBuildParams(),
            builders = mock(defaultAnswer = Answers.RETURNS_MOCKS),
            routingSource = Empty()
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
