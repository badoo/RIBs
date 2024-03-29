package com.badoo.ribs.samples.routing.simple_routing

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.routing.simple_routing.rib.R
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.builder.SimpleRoutingParentBuilder
import org.junit.Rule
import org.junit.Test

class SimpleRoutingParentTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        SimpleRoutingParentBuilder(
            object : SimpleRoutingParent.Dependency {}
        ).build(root(savedInstanceState))

    @Test
    fun showsTitleText() {
        onView(withId(R.id.parent_title))
            .check(matches(withText(R.string.parent_text)))
    }
}
