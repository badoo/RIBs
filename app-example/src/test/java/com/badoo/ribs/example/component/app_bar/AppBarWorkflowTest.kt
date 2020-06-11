package com.badoo.ribs.example.component.app_bar

import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.example.component.app_bar.builder.AppBarBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppBarWorkflowTest {

    private lateinit var workflow: AppBar

    @Before
    fun setup() {
        workflow = AppBarBuilder(object : AppBar.Dependency {

        }).build(root(savedInstanceState = null)).also {
            it.node.onAttach()
        }
    }

    @After
    fun tearDown() {
    }

    /**
     * TODO: Add tests for every workflow action that operates on the node
     */
    @Test
    fun `business logic operation test`() {
        workflow.businessLogicOperation()
        // verify(feature).accept(Wish)

        throw RuntimeException("Add real tests.")
    }

    /**
     * TODO: Add tests for every workflow action that attaches a child to ensure workflow step can be fulfilled
     */
    @Test
    fun `attach child workflow step is fulfillable`() {
        // val testObserver = TestObserver<Child>()

        // workflow.attachChild1().subscribe(testObserver)

        // testObserver.assertValueCount(1)
        // testObserver.assertComplete()

        throw RuntimeException("Add real tests.")
    }
}
