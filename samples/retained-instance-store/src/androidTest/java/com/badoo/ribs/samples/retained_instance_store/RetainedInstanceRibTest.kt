package com.badoo.ribs.samples.retained_instance_store

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.retained_instance_store.rib.RetainedInstanceRib
import com.badoo.ribs.samples.retained_instance_store.rib.builder.RetainedInstanceBuilder
import com.badoo.ribs.samples.retained_instance_store.utils.Clock
import com.badoo.ribs.samples.retained_instance_store.utils.ScreenOrientationController
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RetainedInstanceRibTest {
    @get:Rule
    val ribsRule = RibsRule{activity, savedInstanceState -> buildRib(savedInstanceState, activity)}

    private val clockMock = MockedClock()

    private fun buildRib(savedInstanceState: Bundle?, activity: Activity) =
            RetainedInstanceBuilder(object : RetainedInstanceRib.Dependency {
                override val orientationController = ScreenOrientationController(activity)
                override val clock = clockMock
            }).build(BuildContext.root(savedInstanceState))

    @Test
    fun WHEN_click_on_rotate_button_THEN_screen_rotates() {
        val orientation = ribsRule.activity.requestedOrientation
        onView(ViewMatchers.withId(R.id.rotate_screen_button)).perform(click())
        assertNotEquals(ribsRule.activity.requestedOrientation, orientation)
    }

    @Test
    fun WHEN_view_is_created_THEN_clock_output_is_shown() {
        val seconds = onView(ViewMatchers.withId(R.id.seconds_counter)).getText().toInt()
        assertEquals(clockMock.elapsedTicks, seconds)
    }

    @Test
    fun WHEN_activity_is_recreated_THEN_clock_is_not_reset() {
        clockMock.mockedTicks = 40
        with(ribsRule.activity){
            runOnUiThread {
                recreate()
            }
        }
        val seconds = onView(ViewMatchers.withId(R.id.seconds_counter)).getText().toInt()
        assertEquals(clockMock.elapsedTicks, seconds)
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

    internal class MockedClock: Clock {

        var mockedTicks = 0

        override val elapsedTicks: Int
            get() = mockedTicks

        override fun starTimer(onTick: (counter: Int) -> Unit) {
            //Do nothing
        }

        override fun stopTimer() {
            //Not Required
        }
    }

}