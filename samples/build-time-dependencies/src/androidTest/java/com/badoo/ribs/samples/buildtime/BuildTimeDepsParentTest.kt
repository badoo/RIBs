package com.badoo.ribs.samples.buildtime

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.buildtime.parent.builder.BuildTimeDepsParentBuilder
import org.junit.Rule
import org.junit.Test

class BuildTimeDepsParentTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        BuildTimeDepsParentBuilder().build(root(savedInstanceState))

    @Test
    fun verifyInitialState() {
        onView(withId(R.id.profile_label)).check(doesNotExist())
    }

    @Test
    fun verifyProfileBuiltState() {
        onView(withId(R.id.parent_profile_id_spinner)).perform(click())
        onView(withText("Profile 9")).perform(click())
        onView(withId(R.id.parent_profile_build_button)).perform(click())

        onView(withText("This is the profile for user: 9"))
            .check(matches(isCompletelyDisplayed()))
    }
}
