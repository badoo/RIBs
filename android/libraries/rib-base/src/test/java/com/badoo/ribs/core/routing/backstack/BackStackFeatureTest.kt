package com.badoo.ribs.core.routing.backstack

import com.badoo.mvicore.element.TimeCapsule
import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.State
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.NewRoot
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Pop
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Push
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.PushOverlay
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Replace
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BackStackFeatureTest {

    companion object {
        private val initialConfiguration = Configuration.C1
    }

    private lateinit var timeCapsuleEmpty: TimeCapsule<BackStackFeature.State<TestRouter.Configuration>>
    private lateinit var timeCapsuleWithContent: TimeCapsule<BackStackFeature.State<TestRouter.Configuration>>
    private lateinit var backstackInTimeCapsule: List<Configuration>
    private lateinit var backStackManager: BackStackFeature<Configuration>

    @Before
    fun setUp() {
        backstackInTimeCapsule = listOf(
            Configuration.C3,
            Configuration.C2
        )

        timeCapsuleEmpty = mock()
        timeCapsuleWithContent = mock {
            on { get<State<Configuration>>(BackStackFeature::class.java.name) } doReturn State(backstackInTimeCapsule)
        }
        setupBackStackManager(timeCapsuleEmpty)
    }

    private fun setupBackStackManager(timeCapsule: TimeCapsule<BackStackFeature.State<Configuration>>) {
        backStackManager = BackStackFeature(
            initialConfiguration,
            timeCapsule
        )
    }

    @Test
    fun `Initial back stack contains only one element`() {
        assertEquals(1, backStackManager.state.backStack.size)
    }

    @Test
    fun `Initial state matches initial configuration`() {
        assertEquals(initialConfiguration, backStackManager.state.current)
    }

    @Test
    fun `After state restoration back stack matches the one in the time capsule`() {
        setupBackStackManager(timeCapsuleWithContent)
        assertEquals(backstackInTimeCapsule, backStackManager.state.backStack)
    }

    @Test
    fun `Back stack state's current() references last item`() {
        setupBackStackManager(timeCapsuleWithContent)
        assertEquals(backstackInTimeCapsule.last(), backStackManager.state.current)
    }

    @Test
    fun `Wish_Push once results in the back stack size growing by one`() {
        backStackManager.accept(Push(Configuration.C4))
        assertEquals(2, backStackManager.state.backStack.size)
    }

    @Test
    fun `Wish_Push once adds the expected new element to the end of the back stack`() {
        backStackManager.accept(Push(Configuration.C4))
        assertEquals(Configuration.C4, backStackManager.state.current)
    }

    @Test
    fun `Wish_Push consecutively results in expected backstack content`() {
        backStackManager.accept(Push(Configuration.C2))
        backStackManager.accept(Push(Configuration.C3))
        backStackManager.accept(Push(Configuration.C4))
        backStackManager.accept(Push(Configuration.C5))
        val expected = listOf(
            initialConfiguration,
            Configuration.C2,
            Configuration.C3,
            Configuration.C4,
            Configuration.C5
        )
        assertEquals(expected, backStackManager.state.backStack)
    }

    @Test
    fun `Wish_PushOverlay once results in the back stack size growing by one`() {
        backStackManager.accept(PushOverlay(Configuration.C4))
        assertEquals(2, backStackManager.state.backStack.size)
    }

    @Test
    fun `Wish_PushOverlay once adds the expected new element to the end of the back stack`() {
        backStackManager.accept(PushOverlay(Configuration.C4))
        assertEquals(Configuration.C4, backStackManager.state.current)
    }

    @Test
    fun `Wish_Replace does not change back stack size`() {
        // initial size: 1
        backStackManager.accept(Push(Configuration.C2)) // should increase to 2
        backStackManager.accept(Push(Configuration.C3)) // should increase to 3
        backStackManager.accept(Replace(Configuration.C4)) // should keep 3
        assertEquals(3, backStackManager.state.backStack.size)
    }

    @Test
    fun `Wish_Replace puts the correct configuration at the end of the back stack`() {
        backStackManager.accept(Push(Configuration.C2))
        backStackManager.accept(Push(Configuration.C3))
        backStackManager.accept(Replace(Configuration.C4))
        assertEquals(Configuration.C4, backStackManager.state.current)
    }

    @Test
    fun `Wish_Replace consecutively results in expected backstack content`() {
        backStackManager.accept(Push(Configuration.C2))
        backStackManager.accept(Push(Configuration.C3))
        backStackManager.accept(Replace(Configuration.C4))
        backStackManager.accept(Replace(Configuration.C5))
        val expected = listOf(
            initialConfiguration,
            Configuration.C2,
            Configuration.C5
        )
        assertEquals(expected, backStackManager.state.backStack)
    }

    @Test
    fun `Wish_NewRoot results in new back stack with only one element`() {
        backStackManager.accept(Push(Configuration.C2))
        backStackManager.accept(Push(Configuration.C3))
        backStackManager.accept(NewRoot(Configuration.C4))
        assertEquals(1, backStackManager.state.backStack.size)
    }

    @Test
    fun `Wish_NewRoot puts the correct configuration at the end of the back stack`() {
        backStackManager.accept(Push(Configuration.C2))
        backStackManager.accept(Push(Configuration.C3))
        backStackManager.accept(NewRoot(Configuration.C4))
        assertEquals(Configuration.C4, backStackManager.state.current)
    }

    @Test
    fun `Wish_NewRoot consecutively results in expected backstack content`() {
        backStackManager.accept(Push(Configuration.C2))
        backStackManager.accept(Push(Configuration.C3))
        backStackManager.accept(NewRoot(Configuration.C4))
        backStackManager.accept(NewRoot(Configuration.C5))
        val expected = listOf(
            Configuration.C5
        )
        assertEquals(expected, backStackManager.state.backStack)
    }

    @Test
    fun `Wish_Pop does not change back stack if there's only one entry`() {
        val lastElementBeforePop = backStackManager.state.current
        backStackManager.accept(Pop())
        assertEquals(lastElementBeforePop, backStackManager.state.current)
    }

    @Test
    fun `Wish_Pop reduces size of back stack if there's more than one entry`() {
        // initial size: 1
        backStackManager.accept(Push(Configuration.C2)) // should increase size to: 2
        backStackManager.accept(Push(Configuration.C3)) // should increase size to: 3
        backStackManager.accept(Push(Configuration.C4)) // should increase size to: 4
        backStackManager.accept(Pop())
        assertEquals(3, backStackManager.state.backStack.size)
    }

    @Test
    fun `Wish_Pop results in expected new back stack`() {
        // initial size: 1
        backStackManager.accept(Push(Configuration.C2)) // should increase size to: 2
        backStackManager.accept(Push(Configuration.C3)) // should increase size to: 3
        backStackManager.accept(Push(Configuration.C4)) // should increase size to: 4
        backStackManager.accept(Pop())
        val expected = listOf(
            initialConfiguration,
            Configuration.C2,
            Configuration.C3
        )
        assertEquals(expected, backStackManager.state.backStack)
    }
}
