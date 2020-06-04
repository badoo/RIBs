package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.operation.Replace
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ReplaceTest {

    private lateinit var replace: Replace<Configuration>

    @Test
    fun `not applicable when current element configuration same`() {
        val backStack = listOf(C1, C2).asBackStackElements()
        replace = Replace(C2)

        val applicable = replace.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `not applicable when current element configuration same with different overlay`() {
        val backStack = listOf(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration),
                overlays = listOf(
                    Routing(Configuration.O2 as Configuration)
                )
            )
        )
        replace = Replace(C1)

        val applicable = replace.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `invoke change current element to new one`() {
        val backStack = listOf(C1, C2).asBackStackElements()
        replace = Replace(C3)

        val newBackStack = replace.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(
                routing = Routing(C1 as Configuration)
            ),
            RoutingHistoryElement(
                routing = Routing(C3 as Configuration)
            )
        )
    }
}
