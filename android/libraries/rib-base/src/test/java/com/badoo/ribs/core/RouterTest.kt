package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RouterTest {

    private lateinit var router: TestRouter
    private lateinit var routingActionForC1: RoutingAction
    private lateinit var routingActionForC2: RoutingAction
    private lateinit var routingActionForC3: RoutingAction
    private lateinit var routingActionForC4: RoutingAction
    private lateinit var routingActionForC5: RoutingAction
    private lateinit var node: Node<TestView>
    private lateinit var childNodeC2_1: Node<*>
    private lateinit var childNodeC2_2: Node<*>

    @Before
    fun setUp() {
        Thread.currentThread().setUncaughtExceptionHandler { _, _ -> assert(false) }

        childNodeC2_1 = mock()
        childNodeC2_2 = mock()

        routingActionForC2 = mock { on { buildNodes(any())} doReturn listOf(childNodeC2_1, childNodeC2_2) }
        routingActionForC1 = mock()
        routingActionForC3 = mock()
        routingActionForC4 = mock()
        routingActionForC5 = mock()

        router = TestRouter(
            initialConfiguration = TestRouter.Configuration.C2,
            routingActionForC1 = routingActionForC1,
            routingActionForC2 = routingActionForC2,
            routingActionForC3 = routingActionForC3,
            routingActionForC4 = routingActionForC4,
            routingActionForC5 = routingActionForC5,
            routingActionForO1 = mock(),
            routingActionForO2 = mock(),
            routingActionForO3 = mock(),
            permanentParts = listOf()
        )

        node = mock(defaultAnswer = Answers.RETURNS_MOCKS)
        router.init(node)
    }

    @Test
    fun `Node is correctly set after init`() {
        assertEquals(node, router.node)
    }

    @Test
    fun `Save instance state call reaches child nodes`() {
        router.onAttach()
        router.onSaveInstanceState(mock())
        verify(childNodeC2_1).onSaveInstanceState(any())
        verify(childNodeC2_2).onSaveInstanceState(any())
    }

    @Test
    fun `Pushing another configuration after initial is possible`() {
        router.onAttach()
        router.push(TestRouter.Configuration.C5)
    }
}
