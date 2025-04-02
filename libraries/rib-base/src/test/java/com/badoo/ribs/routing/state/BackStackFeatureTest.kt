package com.badoo.ribs.routing.state

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.minimal.state.TimeCapsule
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.BackStack.State
import com.badoo.ribs.routing.source.backstack.Elements
import com.badoo.ribs.routing.state.feature.operation.asBackStackElements
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

// TODO add more tests:
//  - identifier maintenance
//  - activation maintenance
class BackStackFeatureTest {

    companion object {
        private val initialConfiguration = C1
        private const val ID = 100
    }

    private lateinit var timeCapsuleEmpty: TimeCapsule
    private lateinit var timeCapsuleWithContent: TimeCapsule
    private lateinit var backstackInTimeCapsule: List<RoutingHistoryElement<Configuration>>
    private lateinit var backStack: BackStack<Configuration>

    @Before
    fun setUp() {
        backstackInTimeCapsule = listOf(
            RoutingHistoryElement(Routing(C3 as Configuration)),
            RoutingHistoryElement(Routing(C2 as Configuration))
        )

        timeCapsuleEmpty = mock()
        timeCapsuleWithContent = mock {
            on { get<State<Configuration>>(BackStack::class.java.name) } doReturn State(
                id = ID,
                elements = backstackInTimeCapsule
            )
        }
        setupBackStackManager(timeCapsuleEmpty)
    }

    private fun setupBackStackManager(timeCapsule: TimeCapsule) {
        backStack =
            BackStack(
                initialConfiguration = initialConfiguration,
                timeCapsule = timeCapsule
            )
    }

    @Test
    fun `Initial back stack contains only one element`() {
        assertEquals(1, backStack.state.elements.size)
    }

    @Test
    fun `Initial state matches initial configuration`() {
        assertEquals(initialConfiguration, backStack.state.current!!.routing.configuration)
    }

    @Test
    fun `After state restoration back stack matches the one in the time capsule`() {
        setupBackStackManager(timeCapsuleWithContent)
        assertEquals(backstackInTimeCapsule, backStack.state.elements)
    }

    @Test
    fun `Back stack state's current() references last item`() {
        setupBackStackManager(timeCapsuleWithContent)
        assertEquals(backstackInTimeCapsule.last(), backStack.state.current)
    }

    @Test
    fun `State is updated when operation is applicable`() {
        val newBackStack = listOf(C2, C3)
            .asBackStackElements(true)

        val backStackOperation = backStackOperation(
            isApplicable = { true },
            elementsOperation = { newBackStack }
        )

        backStack.accept(backStackOperation)

        val expected = newBackStack.map { it.routing.configuration }
        val actual = backStack.state.elements.map { it.routing.configuration }

        assertEquals(expected, actual)
    }

    @Test
    fun `State is not updated when operation is not applicable`() {
        val oldBackStack = backStack.state.elements
        val newBackStack = listOf(C2, C3).asBackStackElements(true)
        val backStackOperation = backStackOperation(
            isApplicable = { false },
            elementsOperation = { newBackStack }
        )

        backStack.accept(backStackOperation)

        assertEquals(oldBackStack, backStack.state.elements)
    }

    @Suppress("UNCHECKED_CAST")
    private fun backStackOperation(
        isApplicable: (Elements<Configuration>) -> Boolean = { true },
        elementsOperation: (Elements<Configuration>) -> Elements<Configuration> = { it }
    ): BackStack.Operation<Configuration> =
        mock<BackStack.Operation<Configuration>>().apply {
            whenever(this.isApplicable(any())).thenAnswer { isApplicable(it.arguments[0] as Elements<Configuration>) }
            whenever(this.invoke(any())).thenAnswer { elementsOperation(it.arguments[0] as Elements<Configuration>) }
        }
}
