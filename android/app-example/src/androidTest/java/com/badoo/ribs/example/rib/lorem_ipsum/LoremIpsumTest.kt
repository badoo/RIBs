package com.badoo.ribs.example.rib.lorem_ipsum

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.example.R
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class LoremIpsumTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        LoremIpsumBuilder(object : LoremIpsum.Dependency {
            override fun loremIpsumOutput(): Consumer<LoremIpsum.Output> = Consumer {}
        }).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
         onView(withId(R.id.lorem_ipsum_text)).check(matches(isDisplayed()))
    }
}
