package com.badoo.ribs.template.node.foo_bar.v1

import android.os.Bundle
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.template.node.foo_bar.common.FooBar
import org.junit.Rule
import org.junit.Test

class FooBarTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    // TODO use rib for interactions based on it implementing Connectable<Input, Output>
    lateinit var rib: FooBarRib

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        FooBarBuilder(
            object : FooBar.Dependency {}
        ).build(root(savedInstanceState)).also {
            rib = it
        }

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
