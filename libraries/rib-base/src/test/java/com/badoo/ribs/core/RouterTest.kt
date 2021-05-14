package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.core.view.RibView
import com.nhaarman.mockitokotlin2.any
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
    private lateinit var resolutionForC1: Resolution
    private lateinit var resolutionForC2: Resolution
    private lateinit var resolutionForC3: Resolution
    private lateinit var resolutionForC4: Resolution
    private lateinit var resolutionForC5: Resolution
    private lateinit var node: Node<TestView>
    private lateinit var childNodeC2_1: Node<*>
    private lateinit var childNodeC2_2: Node<*>

    @Before
    fun setUp() {
        Thread.currentThread().setUncaughtExceptionHandler { _, _ -> assert(false) }

        childNodeC2_1 = mock()
        childNodeC2_2 = mock()

        resolutionForC2 = mock { on { buildNodes(any())} doReturn listOf(
            childNodeC2_1.toRib(),
            childNodeC2_2.toRib())
        }
        resolutionForC1 = mock()
        resolutionForC3 = mock()
        resolutionForC4 = mock()
        resolutionForC5 = mock()

        router = TestRouter(
            initialConfiguration = TestRouter.Configuration.C2,
            resolutionForC1 = resolutionForC1,
            resolutionForC2 = resolutionForC2,
            resolutionForC3 = resolutionForC3,
            resolutionForC4 = resolutionForC4,
            resolutionForC5 = resolutionForC5,
            resolutionForO1 = mock(),
            resolutionForO2 = mock(),
            resolutionForO3 = mock()
        )

        node = mock(defaultAnswer = Answers.RETURNS_MOCKS)
        router.init(node)
    }

    private fun <T : RibView> Node<T>.toRib(): Rib = object : Rib {
        override val node: Node<T> = this@toRib
    }

    @Test
    fun `Node is correctly set after init`() {
        assertEquals(node, router.node)
    }

    @Test
    fun `Save instance state call reaches child nodes`() {
        router.onBuild()
        router.onCreate(mock())
        router.onSaveInstanceState(mock())
        verify(childNodeC2_1).onSaveInstanceState(any())
        verify(childNodeC2_2).onSaveInstanceState(any())
    }
}
