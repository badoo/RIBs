package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.routing.action.RoutingAction
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Answers

class RouterTest {

    private lateinit var router: TestRouter
    private lateinit var routingActionForC1: RoutingAction<TestView>
    private lateinit var routingActionForC2: RoutingAction<TestView>
    private lateinit var routingActionForC3: RoutingAction<TestView>
    private lateinit var routingActionForC4: RoutingAction<TestView>
    private lateinit var routingActionForC5: RoutingAction<TestView>
    private lateinit var node: Node<TestView>
    private lateinit var childNodeC2_1: Node<*>
    private lateinit var childNodeC2_2: Node<*>

    @Before
    fun setUp() {
        Thread.currentThread().setUncaughtExceptionHandler { _, _ -> assert(false) }

        childNodeC2_1 = mock()
        childNodeC2_2 = mock()

        val nodeDescriptorsC2 =
            listOf(childNodeC2_1, childNodeC2_2).map {
                Node.Descriptor(it, Node.ViewAttachMode.PARENT)
            }

        routingActionForC2 = mock { on { buildNodes(anyOrNull())} doReturn nodeDescriptorsC2 }
        routingActionForC1 = mock()
        routingActionForC3 = mock()
        routingActionForC4 = mock()
        routingActionForC5 = mock()

        router = TestRouter(
            savedInstanceState = null,
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
        router.node = node
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
