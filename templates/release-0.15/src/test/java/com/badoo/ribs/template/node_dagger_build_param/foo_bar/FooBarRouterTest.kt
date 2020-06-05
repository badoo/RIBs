package com.badoo.ribs.template.node_dagger_build_param.foo_bar

import com.badoo.ribs.core.builder.BuildParams
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FooBarRouterTest {

    private val buildParams: BuildParams<Nothing?> = mock()
    private var router: FooBarRouter? = null

    @Before
    fun setup() {
        router = FooBarRouter(
            buildParams = buildParams
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
