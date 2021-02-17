package com.badoo.ribs.samples.retained_instance_store

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.retained_instance_store.rib.counter.CounterBuilder
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CounterTest {
    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        CounterBuilder().build(BuildContext.root(savedInstanceState))

    @Test
    fun WHEN_view_is_created_THEN_clock_output_is_shown() {
        val seconds = onView(ViewMatchers.withId(R.id.counter1)).getText()
        assertTrue(seconds.isNotEmpty())
    }

    @Test
    fun WHEN_view_is_created_THEN_retained_clock_output_is_shown() {
        val seconds = onView(ViewMatchers.withId(R.id.counter2)).getText()
        assertTrue(seconds.isNotEmpty())
    }

    @Test
    fun WHEN_activity_is_recreated_GIVEN_counter_is_retained_THEN_clock_has_not_reset() {
        val initialSeconds = onView(ViewMatchers.withId(R.id.counter2)).getText().toInt()
        Thread.sleep(1100)
        with(ribsRule.activity) {
            runOnUiThread {
                recreate()
            }
        }
        val endSeconds = onView(ViewMatchers.withId(R.id.counter2)).getText().toInt()
        assertNotEquals(initialSeconds, endSeconds)
    }

    @Test
    fun WHEN_activity_is_recreated_GIVEN_counter_is_not_retained_THEN_clock_has_reset() {
        val initialSeconds = onView(ViewMatchers.withId(R.id.counter1)).getText().toInt()
        with(ribsRule.activity) {
            runOnUiThread {
                recreate()
            }
        }
        val endSeconds = onView(ViewMatchers.withId(R.id.counter1)).getText().toInt()
        assertEquals(initialSeconds, endSeconds)
    }

    private fun ViewInteraction.getText(): String {
        var text = String()
        perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }
}