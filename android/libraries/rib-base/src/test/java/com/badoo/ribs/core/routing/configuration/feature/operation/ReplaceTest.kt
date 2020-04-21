package com.badoo.ribs.core.routing.configuration.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
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
            BackStackElement(C1, listOf(O1))
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
            BackStackElement(C1),
            BackStackElement(C3)
        )
    }
}