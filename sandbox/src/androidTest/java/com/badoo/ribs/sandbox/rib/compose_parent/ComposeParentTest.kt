package com.badoo.ribs.sandbox.rib.compose_parent

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.test.RibsRule
import org.junit.Rule
import org.junit.Test

class ComposeParentTest {

    @get:Rule
    val rule = AndroidComposeTestRule(
        activityRule = RibsRule(builder = { _, bundle ->
            ComposeParentBuilder(object : ComposeParent.Dependency {})
                .build(BuildContext.root(bundle))
        }),
        activityProvider = { it.activity },
    )

    @Test
    fun canLaunchAndDisplay() {
        rule.onNodeWithText("ComposeParentView").assertIsDisplayed()
    }

}
