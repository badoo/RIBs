package com.badoo.ribs.core.routing.configuration

import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeatureState
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStack
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStackOperation
import com.badoo.ribs.core.routing.configuration.feature.operation.asBackStackElements
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
import org.junit.Ignore
import org.junit.Test

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
        backStackFeature = BackStackFeature(
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
    @Ignore("This should be reworked. Routing identifier is now crafted inside BackStackFeature and causes a difference.")
    fun `update state when operation is acceptable`() {
        val newBackStack = listOf(C2, C3).asBackStackElements(true)
        val backStackOperation = backStackOperation { newBackStack }

        backStackFeature.accept(BackStackFeature.Operation(backStackOperation))

        assertEquals(newBackStack, backStackFeature.state.backStack)
    }

    @Test
    fun `stay with previous state when operation is not acceptable`() {
        val newBackStack = listOf(C2, C3).asBackStackElements(true)
        val oldBackStack = backStackFeature.state.backStack
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
