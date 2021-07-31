package com.badoo.ribs.samples.routing.simple_routing

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.routing.simple_routing.rib.R
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.builder.Child1Child1Builder
import org.junit.Rule
import org.junit.Test

class SimpleRoutingChild1Child1Test {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        Child1Child1Builder(
            object : Child1Child1.Dependency {}
        ).build(root(savedInstanceState))

    @Test
    fun showsTitleText() {
        onView(withId(R.id.child1_child1_title))
            .check(matches(withText(R.string.child1_child1_text)))
    }
}
