package com.badoo.ribs.example.component.app_bar

import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.example.component.app_bar.builder.AppBarBuilder
import com.badoo.ribs.example.model.User
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppBarWorkflowTest {

    private lateinit var workflow: AppBar

    @Before
    fun setup() {
        workflow = AppBarBuilder(object : AppBar.Dependency {

        }).build(
            buildContext = root(savedInstanceState = null),
            payload = AppBarBuilder.Params(User.UnauthenticatedUser)
        ).also {
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
     * TODO: Add tests for every workflow action that attaches a child to ensure workflow stepлисател can be fulfilled
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
