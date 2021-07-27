package com.badoo.ribs.test.view

import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.ribs.test.sample.SampleViewImpl
import com.badoo.ribs.test.sample.viewId
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

abstract class RibsViewRuleTestBase(
    private val invokeStart: Boolean
) {

    @get:Rule
    abstract val rule: RibsViewRule<SampleViewImpl>

    @Test
    fun themeIsAppliedCorrectly() {
        if (invokeStart) rule.start()
        val lp = rule.activity.window.attributes
        // Theme_AppCompat_Dialog has FLAG_DIM_BEHIND, but default theme does not
        Assert.assertTrue(lp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND == WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    @Test
    fun viewIsVisible() {
        if (invokeStart) rule.start()

        onView(withId(viewId)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun resumedStateAfterStart() {
        if (invokeStart) rule.start()

        assertEquals(Lifecycle.State.RESUMED, rule.view.lifecycle.currentState)
    }

    @Test
    fun destroyedStateAfterFinished() {
        if (invokeStart) rule.start()
        val view = rule.view
        rule.finishActivity()

        // wait for activity finishes
        InstrumentationRegistry
            .getInstrumentation()
            .waitForIdle {
                assertEquals(Lifecycle.State.DESTROYED, view.lifecycle.currentState)
            }
    }

    @Test
    fun viewIsAccessibleAsProperty() {
        if (invokeStart) rule.start()

        assertNotNull(rule.view)
    }

}
