package com.badoo.ribs.template.node_dagger.foo_bar

import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.template.node_dagger.foo_bar.builder.FooBarBuilder
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarWorkflowTest {

    private lateinit var workflow: FooBar

    @Before
    fun setup() {
        workflow = FooBarBuilder(object : FooBar.Dependency {
            override val fooBarInput: ObservableSource<FooBar.Input> = mock()
            override val fooBarOutput: Consumer<FooBar.Output> = mock()
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
        // val testObserver = TestObserver<Child.Workflow>()

        // workflow.attachChild1().subscribe(testObserver)

        // testObserver.assertValueCount(1)
        // testObserver.assertComplete()

        throw RuntimeException("Add real tests.")
    }
}
