package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O2
import com.badoo.ribs.core.helper.TestRouter.Configuration.O3
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.Routing.Identifier
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.Elements
import com.badoo.ribs.routing.source.backstack.operation.Remove
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RemoveTest {

    private lateinit var remove: Remove<Configuration>

    val elements: Elements<Configuration> = listOf<RoutingHistoryElement<Configuration>>(
        RoutingHistoryElement(
            routing = Routing<Configuration>(
                identifier = Identifier(id = 10),
                configuration = C1
            )
        ),
        RoutingHistoryElement(
            routing = Routing<Configuration>(
                identifier = Identifier(id = 20),
                configuration = C2
            ),
            overlays = listOf(
                Routing<Configuration>(
                    identifier = Identifier(id = 201),
                    configuration = O1
                )
            )
        ),
        RoutingHistoryElement(
            routing = Routing<Configuration>(
                identifier = Identifier(id = 30),
                configuration = C3
            ),
            overlays = listOf(
                Routing<Configuration>(
                    identifier = Identifier(id = 301),
                    configuration = O1
                ),
                Routing<Configuration>(
                    identifier = Identifier(id = 302),
                    configuration = O2
                ),
                Routing<Configuration>(
                    identifier = Identifier(id = 303),
                    configuration = O3
                )
            )
        )
    )

    @Test
    fun `Remove isn't applicable when identifier isn't found in history`() {
        remove = Remove(Identifier(id = -1))

        val applicable = remove.isApplicable(elements)

        assertThat(applicable).isEqualTo(false)
    }

    @Test
    fun `Remove is applicable when back stack has a content element with target identifier`() {
        remove = Remove(Identifier(id = 20))

        val applicable = remove.isApplicable(elements)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `Remove is applicable when back stack has an overlay element with target identifier`() {
        remove = Remove(Identifier(id = 302))

        val applicable = remove.isApplicable(elements)

        assertThat(applicable).isEqualTo(true)
    }

    @Test
    fun `Remove returns original back stack if identifier isn't found`() {
        remove = Remove(Identifier(id = -1))

        val result = remove.invoke(elements)
        val expected = elements

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `Remove removes correct content element with target identifier`() {
        remove = Remove(Identifier(id = 20))

        val result = remove.invoke(elements)
        val expected = listOf(elements[0], elements[2])

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Remove removes correct overlay element with target identifier`() {
        remove = Remove(Identifier(id = 302))

        val result = remove.invoke(elements)
        val expected = listOf(elements[0], elements[1], elements[2].copy(
            overlays = listOf(
                elements[2].overlays[0],
                elements[2].overlays[2]
            )
        ))

        assertThat(result).isEqualTo(expected)
    }
}
