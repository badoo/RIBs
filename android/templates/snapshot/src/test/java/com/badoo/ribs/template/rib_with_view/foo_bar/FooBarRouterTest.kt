package com.badoo.ribs.template.rib_with_view.foo_bar

import com.badoo.ribs.core.BuildContext
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FooBarRouterTest {

    private val buildContext: BuildContext<Nothing?> = mock()
    private var router: FooBarRouter? = null

    @Before
    fun setup() {
        router = FooBarRouter(
            buildContext = buildContext
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
