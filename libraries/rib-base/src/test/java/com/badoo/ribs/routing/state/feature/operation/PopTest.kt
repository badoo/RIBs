package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O2
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.operation.Pop
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PopTest {

    private val pop: Pop<Configuration> = Pop()

    @Test
    fun `not applicable when backStack contains single element without overlays`() {
        val backStack = listOf(C1).asBackStackElements()

        val applicable = pop.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `applicable when backStack contains single element with overlays`() {
        val backStack = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration)
                )
            )
        )

        val applicable = pop.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `applicable when backStack contains more than one element`() {
        val backStack = listOf(C1, C2).asBackStackElements()

        val applicable = pop.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `invoke remove last content when no overlay on top`() {
        val backStack = listOf(C1, C2).asBackStackElements()

        val newBackStack = pop.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration)
            )
        )
    }

    @Test
    fun `invoke remove last overlay when overlay on top`() {
        //last element with two overlays
        val backStack = listOf<RoutingHistoryElement<Configuration>>(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration)
            ),
            RoutingHistoryElement(
                routing = Routing(C2 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration),
                    Routing(O2 as Configuration)
                )
            )
        )

        val newBackStack = pop.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration)
            ),
            RoutingHistoryElement(
                routing = Routing(C2 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration)
                )
            )
        )
    }
}
