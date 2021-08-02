package com.badoo.ribs.sandbox.rib

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentBuilder
import com.badoo.ribs.test.RibsRule
import org.junit.Rule
import org.junit.Test

class BasicComposeTest {

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
