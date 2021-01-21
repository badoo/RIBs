package com.badoo.ribs.sandbox.rib.dialog_example

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import org.junit.Rule
import org.junit.Test

class DialogExampleTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        DialogExampleBuilder(object : DialogExample.Dependency {
            override val dialogLauncher: DialogLauncher = ribTestActivity.integrationPoint
        }).build(root(savedInstanceState))

    @Test
    fun testSomething() {
    }
}
