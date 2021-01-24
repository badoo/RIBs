package com.badoo.ribs.sandbox.rib.dialog_example

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.matcher.withTextColor
import org.junit.Rule
import org.junit.Test


class DialogExampleTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        DialogExampleBuilder(object : DialogExample.Dependency {
            override val dialogLauncher: DialogLauncher = ribTestActivity.integrationPoint
        }).build(root(savedInstanceState))

    @Test
    fun showsSimpleDialog() {
        onView(withId(R.id.show_simple_dialog)).perform(click())

        onView(withText(R.string.show_simple_dialog))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun showsThemedDialog() {
        onView(withId(R.id.show_themed_dialog)).perform(click())

        onView(withText(R.string.show_themed_dialog))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        // Positive button
        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(withTextColor(R.color.blue)))

        // Negative button
        onView(withId(android.R.id.button2))
            .inRoot(isDialog())
            .check(matches(withTextColor(R.color.red)))
    }

    @Test
    fun showsRibDialog() {
        onView(withId(R.id.show_rib_dialog)).perform(click())

        onView(withText(R.string.a_title_you_wish))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withId(R.id.lorem_ipsum_button))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun showsLazyDialog() {
        onView(withId(R.id.show_lazy_dialog)).perform(click())

        onView(withText(R.string.lazy_dialog_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

}
