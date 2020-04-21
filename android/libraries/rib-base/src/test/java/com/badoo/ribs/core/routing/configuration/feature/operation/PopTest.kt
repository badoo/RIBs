package com.badoo.ribs.core.routing.configuration.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O2
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
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
            BackStackElement(C1, listOf(O1))
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

        assertThat(newBackStack).containsExactly(BackStackElement(C1))
    }

    @Test
    fun `invoke remove last overlay when overlay on top`() {
        //last element with two overlays
        val backStack = listOf<BackStackElement<Configuration>>(
            BackStackElement(C1),
            BackStackElement(C2, listOf(O1, O2))
        )

        val newBackStack = pop.invoke(backStack)

        assertThat(newBackStack).containsExactly(
            BackStackElement(C1),
            BackStackElement(C2, listOf(O1))
        )
    }
}