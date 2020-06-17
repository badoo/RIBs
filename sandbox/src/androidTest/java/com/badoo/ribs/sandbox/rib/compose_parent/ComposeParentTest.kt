package com.badoo.ribs.sandbox.rib.compose_parent

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import org.junit.Rule
import org.junit.Test

class ComposeParentTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    // TODO use rib for interactions based on it implementing Connectable<Input, Output>
    lateinit var rib: ComposeParent

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        ComposeParentBuilder(
            object : ComposeParent.Dependency {}
        ).build(root(savedInstanceState)).also {
            rib = it
        }

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
