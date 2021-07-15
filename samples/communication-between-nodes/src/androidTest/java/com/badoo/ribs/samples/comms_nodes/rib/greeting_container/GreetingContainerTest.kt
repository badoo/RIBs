package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.comms_nodes.R
import com.badoo.ribs.test.RibsRule
import org.junit.Rule
import org.junit.Test

class GreetingContainerTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        GreetingContainerBuilder(
            object : GreetingContainer.Dependency {}
        ).build(BuildContext.root(savedInstanceState))

    @Test
    fun iSeeHello() {
        iSeeWithText("Hello!")
    }

    @Test
    fun changeToFrenchThenISeeBonjour() {
        onView(withId(R.id.change_language_button)).perform(click())
        onView(withText("French")).perform(click())
        onView(withId(R.id.confirm_language_button)).perform(click())
        iSeeWithText("Bonjour")
    }

    private fun iSeeWithText(text: String) {
        onView(withId(R.id.greeting_text)).check(matches(withText(text)))
    }
}
