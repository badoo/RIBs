package com.badoo.ribs.example.rib.dialog_example

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import org.junit.Rule
import org.junit.Test

class DialogExampleTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        DialogExampleBuilder(object : DialogExample.Dependency {
            override fun dialogLauncher(): DialogLauncher = ribTestActivity
        }).build(root(savedInstanceState))

    @Test
    fun testSomething() {
    }
}
