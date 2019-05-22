package com.badoo.ribs.example.rib.dialog_example

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.app.AppRibCustomisations
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import org.junit.Rule
import org.junit.Test

class DialogExampleTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        DialogExampleBuilder(object : DialogExample.Dependency {
            override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
            override fun dialogLauncher(): DialogLauncher = ribTestActivity
        }).build()

    @Test
    fun testSomething() {
    }
}
