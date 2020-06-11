package com.badoo.ribs.example.component.app_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.RoutingSource
import com.badoo.ribs.example.component.app_bar.routing.AppBarChildBuilders
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class AppBarRouterTest {

    private val buildParams: BuildParams<Nothing?> = mock()
    private val builder: AppBarChildBuilders = mock()
    private var router: AppBarRouter? = null

    @Before
    fun setup() {
        router = AppBarRouter(
            buildParams = buildParams,
            routingSource = RoutingSource.Empty(),
            builders = builder
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
