package com.badoo.ribs.samples.routing.simple_routing

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.routing.simple_routing.rib.R
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.Child2
import com.badoo.ribs.samples.routing.simple_routing.rib.child2.builder.Child2Builder
import org.junit.Rule
import org.junit.Test

class SimpleRoutingChild2Test {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        Child2Builder(
            object : Child2.Dependency {}
        ).build(root(savedInstanceState))

    @Test
    fun showsStaticText() {
        onView(withId(R.id.content_text))
            .check(matches(withText(R.string.child2_text)))
    }
}
