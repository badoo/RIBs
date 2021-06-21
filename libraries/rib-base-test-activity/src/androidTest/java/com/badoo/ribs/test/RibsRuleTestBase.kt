package com.badoo.ribs.test

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.test.sample.SampleBuilder
import com.badoo.ribs.test.sample.SampleRib
import com.badoo.ribs.test.sample.viewId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

abstract class RibsRuleTestBase(
    private val invokeStart: Boolean
) {

    @get:Rule
    abstract val rule: RibsRule<SampleRib>

    protected val builder = { _: RibTestActivity, bundle: Bundle? ->
        SampleBuilder().build(
            buildContext = BuildContext.root(bundle),
            payload = null,
        )
    }

    private fun start() {
        rule.start(builder)
    }

    @Test
    fun themeIsAppliedCorrectly() {
        if (invokeStart) start()
        val lp = rule.activity.window.attributes
        // Theme_AppCompat_Dialog has FLAG_DIM_BEHIND, but default theme does not
        assertTrue(lp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND == WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    @Test
    fun viewIsVisible() {
        if (invokeStart) start()

        onView(withId(viewId)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun resumedStateAfterStart() {
        if (invokeStart) start()

        assertEquals(Lifecycle.State.RESUMED, rule.rib.node.lifecycle.currentState)
    }

    @Test
    fun destroyedStateAfterFinished() {
        if (invokeStart) start()
        val rib = rule.rib
        rule.finishActivity()

        waitFor { Lifecycle.State.DESTROYED == rib.node.lifecycle.currentState }
    }

    @Test
    fun ribIsAccessibleAsProperty() {
        if (invokeStart) start()

        assertNotNull(rule.rib)
    }

}
