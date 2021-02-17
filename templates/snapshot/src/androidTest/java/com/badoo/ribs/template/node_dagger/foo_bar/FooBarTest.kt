package com.badoo.ribs.template.node_dagger.foo_bar

import android.os.Bundle
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.template.node_dagger.foo_bar.builder.FooBarBuilder
import org.junit.Rule
import org.junit.Test

class FooBarTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        FooBarBuilder(object : FooBar.Dependency {}).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
