package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.operation.Push
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PushTest {

    private lateinit var push: Push<Configuration>

    @Test
    fun `not applicable when current element with same configuration`() {
        val backStack = listOf(C1).asBackStackElements()
        push = Push(C1)

        val applicable = push.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `not applicable when current element same with different overlays`() {
        val backStack = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(Configuration.O2 as Configuration)
                )
            )
        )
        push = Push(C1)

        val applicable = push.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `applicable when current element with different configuration`() {
        val backStack = listOf(C1, C2).asBackStackElements()
        push = Push(C1)

        val applicable = push.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `invoke add configuration when push`() {
        val backStack = listOf(C1).asBackStackElements()
        push = Push(C2)

        val newBackStack = push.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration)
            ),
            RoutingHistoryElement(
                routing = Routing(C2 as Configuration)
            )
        )
    }
}
