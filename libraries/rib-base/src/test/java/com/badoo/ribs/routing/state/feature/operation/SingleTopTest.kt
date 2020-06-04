package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
import com.badoo.ribs.core.helper.TestRouter.Configuration.C5
import com.badoo.ribs.core.helper.TestRouter.Configuration.C6
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.operation.SingleTop
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SingleTopTest {
    private lateinit var singleTop: SingleTop<Configuration>

    @Test
    fun `applicable when backStack same`() {
        val backStack = listOf(C1).asBackStackElements()
        singleTop = SingleTop(C1)

        val applicable = singleTop.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `applicable when backStack different`() {
        val backStack = listOf(C1, C2).asBackStackElements()
        singleTop = SingleTop(C1)

        val applicable = singleTop.isApplicable(backStack)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `reactivates configuration when found in back stack (object)`() {
        reactivatesConfigurationWhenInBackStack { C3 }
    }

    @Test
    fun `reactivates configuration when found in back stack (data class)`() {
        reactivatesConfigurationWhenInBackStack { C6(1) }
    }

    @Test
    fun `reactivate goes back only until latest occurrence (object)`() {
        reactivatesConfigurationWhenSeveralInBackStack { C3 }
    }

    @Test
    fun `reactivate goes back only until latest occurrence (data class)`() {
        reactivatesConfigurationWhenSeveralInBackStack { C6(1) }
    }

    @Test
    fun `replaces configuration when found in back stack with different parameters`() {
        val initialConfiguration = C6(1)
        val newConfiguration = C6(2)
        val backStack = listOf(C1, initialConfiguration, C4).asBackStackElements()
        singleTop = SingleTop(newConfiguration)

        val newBackStack = singleTop.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(Routing(C1 as Configuration)),
            RoutingHistoryElement(Routing(newConfiguration as Configuration))
        )
    }

    @Test
    fun `invoke add new when backStack doesn't contain new configuration`() {
        val backStack = listOf(C1, C2).asBackStackElements()
        singleTop = SingleTop(C3)

        val newBackStack = singleTop.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            RoutingHistoryElement(Routing(C1 as Configuration)),
            RoutingHistoryElement(Routing(C2 as Configuration)),
            RoutingHistoryElement(Routing(C3 as Configuration))
        )
    }

    private fun reactivatesConfigurationWhenInBackStack(configuration: () -> Configuration) {
        val initialConfiguration = configuration()
        val newConfiguration = configuration()
        val backStack = listOf(C1, C2, initialConfiguration, C4, C5).asBackStackElements()
        val singleTop = SingleTop(newConfiguration)

        val invoke = singleTop.invoke(backStack)

        assertThat(invoke).containsExactly(
            RoutingHistoryElement(Routing(C1 as Configuration)),
            RoutingHistoryElement(Routing(C2 as Configuration)),
            RoutingHistoryElement(Routing(newConfiguration))
        )
    }

    private fun reactivatesConfigurationWhenSeveralInBackStack(configuration: () -> Configuration) {
        val initialConfiguration = configuration()
        val newConfiguration = configuration()
        val backStack = listOf(
            C1, initialConfiguration, C4, initialConfiguration, C5
        ).asBackStackElements()
        val singleTop = SingleTop(newConfiguration)

        val invoke = singleTop.invoke(backStack)

        assertThat(invoke).containsExactly(
            RoutingHistoryElement(Routing(C1 as Configuration)),
            RoutingHistoryElement(Routing(initialConfiguration)),
            RoutingHistoryElement(Routing(C4 as Configuration)),
            RoutingHistoryElement(Routing(newConfiguration))
        )
    }
}
