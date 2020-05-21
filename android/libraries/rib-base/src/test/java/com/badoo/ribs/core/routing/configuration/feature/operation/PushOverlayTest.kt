package com.badoo.ribs.core.routing.configuration.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O2
import com.badoo.ribs.core.routing.history.RoutingElement
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PushOverlayTest {

    private lateinit var pushOverlay: PushOverlay<Configuration>

    @Test
    fun `not applicable when current overlay same`() {
        val backStack: BackStack<Configuration> = listOf(
            RoutingElement(C1, listOf(O1))
        )
        pushOverlay = PushOverlay(O1)

        val applicable = pushOverlay.isApplicable(backStack)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `applicable when current element with different configuration`() {
        val backStack = listOf(
            RoutingElement(C1, listOf(O1))
        )
        pushOverlay = PushOverlay(O2)

        val applicable = pushOverlay.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `invoke add configuration when push`() {
        val backStack = listOf(
            RoutingElement(C1, listOf(O1))
        )
        pushOverlay = PushOverlay(O2)

        val newBackStack = pushOverlay.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingElement(
                C1,
                listOf(O1, O2)
            )
        )
    }
}
