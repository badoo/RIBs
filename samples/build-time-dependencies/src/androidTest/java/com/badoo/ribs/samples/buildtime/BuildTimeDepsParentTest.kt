package com.badoo.ribs.samples.buildtime

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
    fun verifyProfileTextInitiallyHidden() {
        onView(withId(R.id.profile_label)).check(doesNotExist())
    }

    @Test
    fun whenBuildChildWithValidProfileId_verifyProfileTextDisplayed() {
        buildChild("9")

        onView(withId(R.id.profile_label))
            .check(matches(withText("This is the profile for user: 9")))
    }

    @Test
    fun givenProfileTextDisplayed_whenBuildChildWithInvalidProfileId_verifyProfileTextHidden() {
        buildChild("9")

        buildChild(Long.MAX_VALUE.toString())

        onView(withId(R.id.profile_label)).check(doesNotExist())
    }

    private fun buildChild(profileId: String) {
        onView(withId(R.id.parent_profile_id_entry)).perform(replaceText(profileId))
        onView(withId(R.id.parent_profile_build_button)).perform(click())
    }
}
