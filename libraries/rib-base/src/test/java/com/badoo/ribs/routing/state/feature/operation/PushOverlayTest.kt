package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O2
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.Elements
import com.badoo.ribs.routing.source.backstack.operation.PushOverlay
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PushOverlayTest {

    private lateinit var pushOverlay: PushOverlay<Configuration>

    @Test
    fun `not applicable when current overlay same`() {
        val elements: Elements<Configuration> = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration)
                )
            )
        )
        pushOverlay = PushOverlay(O1)

        val applicable = pushOverlay.isApplicable(elements)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `applicable when current element with different configuration`() {
        val backStack = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration)
                )
            )
        )
        pushOverlay = PushOverlay(O2)

        val applicable = pushOverlay.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `invoke add configuration when push`() {
        val backStack = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration)
                )
            )
        )
        pushOverlay = PushOverlay(O2)

        val newBackStack = pushOverlay.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(O1 as Configuration),
                    Routing(O2 as Configuration)
                )
            )
        )
    }
}
