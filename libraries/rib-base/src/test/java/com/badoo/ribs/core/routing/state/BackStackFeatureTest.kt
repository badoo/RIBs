package com.badoo.ribs.core.routing.state

import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.core.routing.source.backstack.BackStackFeatureState
import com.badoo.ribs.core.routing.source.backstack.BackStack
import com.badoo.ribs.core.routing.source.backstack.operation.BackStackOperation
import com.badoo.ribs.core.routing.state.feature.operation.asBackStackElements
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

// TODO add more tests:
//  - identifier maintenance
//  - activation maintenance
class BackStackFeatureTest {

    companion object {
        private val initialConfiguration = C1
    }

    private lateinit var timeCapsuleEmpty: AndroidTimeCapsule
    private lateinit var timeCapsuleWithContent: AndroidTimeCapsule
    private lateinit var backstackInTimeCapsule: List<RoutingHistoryElement<Configuration>>
    private lateinit var backStackFeature: BackStackFeature<Configuration>

    @Before
    fun setUp() {
        backstackInTimeCapsule = listOf(
            RoutingHistoryElement(Routing(C3 as Configuration)),
            RoutingHistoryElement(Routing(C2 as Configuration))
        )

        timeCapsuleEmpty = mock()
        timeCapsuleWithContent = mock {
            on { get<BackStackFeatureState<Configuration>>(BackStackFeature::class.java.name) } doReturn BackStackFeatureState(
                backstackInTimeCapsule
            )
        }
        setupBackStackManager(timeCapsuleEmpty)
    }

    private fun setupBackStackManager(timeCapsule: AndroidTimeCapsule) {
        backStackFeature =
            BackStackFeature(
                initialConfiguration = initialConfiguration,
                timeCapsule = timeCapsule
            )
    }

    @Test
    fun `Initial back stack contains only one element`() {
        assertThat(backStackFeature.state.backStack, hasSize(1))
    }

    @Test
    fun `Initial state matches initial configuration`() {
        assertEquals(initialConfiguration, backStackFeature.state.current!!.routing.configuration)
    }

    @Test
    fun `After state restoration back stack matches the one in the time capsule`() {
        setupBackStackManager(timeCapsuleWithContent)
        assertEquals(backstackInTimeCapsule, backStackFeature.state.backStack)
    }

    @Test
    fun `Back stack state's current() references last item`() {
        setupBackStackManager(timeCapsuleWithContent)
        assertEquals(backstackInTimeCapsule.last(), backStackFeature.state.current)
    }

    @Test
    fun `State is updated when operation is applicable`() {
        val newBackStack = listOf(C2, C3)
            .asBackStackElements(true)

        val backStackOperation = backStackOperation(
            isApplicable = { true },
            backStackOperation = { newBackStack }
        )

        backStackFeature.accept(BackStackFeature.Operation(backStackOperation))

        val expected = newBackStack.map { it.routing.configuration }
        val actual = backStackFeature.state.backStack.map { it.routing.configuration }

        assertEquals(expected, actual)
    }

    @Test
    fun `State is not updated when operation is not applicable`() {
        val oldBackStack = backStackFeature.state.backStack
        val newBackStack = listOf(C2, C3).asBackStackElements(true)
        val backStackOperation = backStackOperation(
            isApplicable = { false },
            backStackOperation = { newBackStack }
        )

        backStackFeature.accept(BackStackFeature.Operation(backStackOperation))

        assertEquals(oldBackStack, backStackFeature.state.backStack)
    }

    @Suppress("UNCHECKED_CAST")
    private fun backStackOperation(
        isApplicable: (BackStack<Configuration>) -> Boolean = { true },
        backStackOperation: (BackStack<Configuration>) -> BackStack<Configuration> = { it }
    ): BackStackOperation<Configuration> =
        mock<BackStackOperation<Configuration>>().apply {
            whenever(this.isApplicable(any())).thenAnswer { isApplicable(it.arguments[0] as BackStack<Configuration>) }
            whenever(this.invoke(any())).thenAnswer { backStackOperation(it.arguments[0] as BackStack<Configuration>) }
        }
}
