package com.badoo.ribs.template.leaf.foo_bar

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import org.junit.Rule
import org.junit.Test

class FooBarTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private val connector = FooBar.Connector()

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        FooBarBuilder(object : FooBar.Dependency {
            override fun fooBarConnector(): FooBar.Connector = connector
        }).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
