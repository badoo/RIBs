package com.badoo.ribs.samples.comms_nodes_1.app.container

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import org.junit.Rule
import org.junit.Test

class containerTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    // TODO use rib for interactions based on it implementing Connectable<Input, Output>
    lateinit var rib: container

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        containerBuilder(
            object : container.Dependency {}
        ).build(root(savedInstanceState)).also {
            rib = it
        }

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
