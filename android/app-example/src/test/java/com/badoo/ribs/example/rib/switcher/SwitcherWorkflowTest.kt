package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.example.rib.blocker.BlockerView
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView
import com.badoo.ribs.example.rib.foo_bar.FooBarNode
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.example.rib.util.TestNode
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SwitcherWorkflowTest {

    private lateinit var workflow: Switcher.Workflow
    private lateinit var router: SwitcherRouter
    private lateinit var interactor: SwitcherInteractor

    @Before
    fun setup() {
        val helloWorldNode = HelloWorldNode(mock(), mock(), mock(), BuildParams.Empty())
        val fooBarNode = FooBarNode(mock(), mock(), BuildParams.Empty(), emptySet())
        val node1 = Node<DialogExampleView>(BuildParams.Empty(), mock(), mock(), mock(), mock())
        val node2 = Node<BlockerView>(BuildParams.Empty(), mock(), mock(), mock(), mock())
        val node3 = Node<MenuView>(BuildParams.Empty(), mock(), mock(), mock(), mock())

        router = SwitcherRouter(
            buildParams = BuildParams.Empty(),
            fooBarBuilder = mock { on { build(any()) } doReturn fooBarNode },
            helloWorldBuilder = mock { on { build(any()) } doReturn helloWorldNode },
            dialogExampleBuilder = mock { on { build(any()) } doReturn node1 },
            blockerBuilder = mock { on { build(any()) } doReturn node2 },
            menuBuilder = mock { on { build(any()) } doReturn node3 },
            dialogLauncher = mock(),
            dialogToTestOverlay = mock()
        )
        interactor = SwitcherInteractor(BuildParams.Empty(), mock(), mock())

        workflow = SwitcherNode(
            buildParams = BuildParams.Empty(),
            viewFactory = mock(),
            router = router,
            interactor = interactor
        ).also { it.onAttach() }
    }

    @Test
    fun `attachHelloWorld`() {
        val testObserver = TestObserver<HelloWorld.Workflow>()

        workflow.attachHelloWorld().subscribe(testObserver)

        testObserver.assertValueCount(1)
        testObserver.assertComplete()
    }

    @Test
    fun `testCrash`() {
        val testObserver = TestObserver<HelloWorld.Workflow>()

        workflow.testCrash().subscribe(testObserver)

        testObserver.assertError(Throwable::class.java)
    }

    @Test
    fun `waitForHelloWorld - hello is already attached`() {
        val testObserver = TestObserver<HelloWorld.Workflow>()

        router.push(Content.Hello)
        workflow.waitForHelloWorld().subscribe(testObserver)

        testObserver.assertValueCount(1)
        testObserver.assertComplete()
    }

    @Test
    fun `waitForHelloWorld - hello is attached after`() {
        val testObserver = TestObserver<HelloWorld.Workflow>()

        workflow.waitForHelloWorld().subscribe(testObserver)
        router.push(Content.Hello)

        testObserver.assertValueCount(1)
        testObserver.assertComplete()
    }

    @Test
    fun `waitForHelloWorld - hello is not attached`() {
        val testObserver = TestObserver<HelloWorld.Workflow>()

        workflow.waitForHelloWorld().subscribe(testObserver)

        testObserver.assertValueCount(0)
        testObserver.assertNotComplete()
    }

    @Test
    fun `doSomethingAndStayOnThisNode`() {
        val testObserver = TestObserver<Switcher.Workflow>()

        workflow.doSomethingAndStayOnThisNode().subscribe(testObserver)

        testObserver.assertValueCount(1)
        testObserver.assertComplete()
    }
}
