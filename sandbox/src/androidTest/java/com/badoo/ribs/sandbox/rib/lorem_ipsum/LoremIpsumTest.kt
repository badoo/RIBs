package com.badoo.ribs.sandbox.rib.lorem_ipsum

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.sandbox.R
import org.junit.Rule
import org.junit.Test

class LoremIpsumTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        LoremIpsumBuilder(object : LoremIpsum.Dependency {}).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
         onView(withId(R.id.lorem_ipsum_text)).check(matches(isDisplayed()))
    }
}
