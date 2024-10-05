package com.badoo.ribs.template.leaf.foo_bar.v1

import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.template.leaf.foo_bar.common.FooBar
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarWorkflowTest {

    private lateinit var workflow: FooBarRib

    @Before
    fun setup() {
        workflow = FooBarBuilder(object : FooBar.Dependency {
        }).build(BuildContext.root(savedInstanceState = null)).also {
            it.node.onCreate()
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
}
