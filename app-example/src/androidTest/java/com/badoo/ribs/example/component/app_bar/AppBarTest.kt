package com.badoo.ribs.example.component.app_bar

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.example.component.app_bar.builder.AppBarBuilder
import org.junit.Rule
import org.junit.Test

class AppBarTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        AppBarBuilder(object : AppBar.Dependency {}).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
