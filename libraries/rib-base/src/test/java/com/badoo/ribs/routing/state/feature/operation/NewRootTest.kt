package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.operation.NewRoot
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NewRootTest {

    private lateinit var newRoot: NewRoot<Configuration>

    @Test
    fun `not applicable when backStack same as newRoot`() {
        val backStack = listOf(C1).asBackStackElements()
        newRoot = NewRoot(C1)

        val applicable = newRoot.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `not applicable when backStack same configuration with overlays`() {
        val backStack = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(Routing(O1 as Configuration))
            )

        )
        newRoot = NewRoot(C1)

        val applicable = newRoot.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `applicable when configuration different`() {
        val backStack = listOf(C1, C2).asBackStackElements()
        newRoot = NewRoot(C1)

        val applicable = newRoot.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `single top configuration when invoked`() {
        val configuration: Configuration = C4
        val backStack = listOf(configuration, C2, C3).asBackStackElements()
        newRoot = NewRoot(configuration)

        val newBackStack = newRoot.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(
                Routing(
                    configuration
                )
            )
        )
    }
}
