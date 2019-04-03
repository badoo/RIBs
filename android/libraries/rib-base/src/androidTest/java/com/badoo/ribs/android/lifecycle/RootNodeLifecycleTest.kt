package com.badoo.ribs.android.lifecycle

import android.arch.lifecycle.Lifecycle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.test.util.LifecycleObserver
import com.badoo.ribs.test.util.ribs.TestRib
import com.badoo.ribs.test.util.ribs.builder.TestRibBuilder
import com.badoo.ribs.test.util.waitForDestroy
import org.junit.Rule
import org.junit.Test

class RootNodeLifecycleTest {

    private val viewLifecycleObserver = LifecycleObserver()
    private val nodeLifecycleObserver = LifecycleObserver()
    private val node = TestRibBuilder(
        object : TestRib.Dependency {
            override fun viewLifecycleObserver() = viewLifecycleObserver
            override fun nodeLifecycleObserver() = nodeLifecycleObserver
        }
    ).build()

    @get:Rule
    val ribsRule = RibsRule { node }
    @Test
    fun whenActivityResumedAndDestroyed_lifecycleEventsAreDispatched() {
        val activity = ribsRule.activity

        viewLifecycleObserver.assertValues(
            Lifecycle.Event.ON_CREATE,
            Lifecycle.Event.ON_START,
            Lifecycle.Event.ON_RESUME
        )

        viewLifecycleObserver.clear()
        ribsRule.finishActivity()

        activity.waitForDestroy()

        viewLifecycleObserver.assertValues(
            Lifecycle.Event.ON_PAUSE,
            Lifecycle.Event.ON_STOP,
            Lifecycle.Event.ON_DESTROY
        )
    }

    // Node attach - detach + view

    // RibActivity

    // Inside a node - attach + view

    // Inside a node - detach + view

    // Inside a node popBack - detach + view

    // After saved instance

    // permanent parts
    // backstack
    // last step backstack
    // overlay + last content
    // non-view-parented children
}
