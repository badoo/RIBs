package com.badoo.ribs.samples.routing.simple_routing

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.routing.simple_routing.rib.R
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1.SimpleRoutingChild1
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_child1.builder.SimpleRoutingChild1Builder
import org.junit.Rule
import org.junit.Test

private const val TITLE = "Any title"

class SimpleRoutingChild1Test {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        SimpleRoutingChild1Builder(
            object : SimpleRoutingChild1.Dependency {
                override val title: String = TITLE
            }
        ).build(root(savedInstanceState))

    @Test
    fun showsTitleText() {
        onView(withId(R.id.child1_title))
            .check(matches(withText(TITLE)))
    }
}
