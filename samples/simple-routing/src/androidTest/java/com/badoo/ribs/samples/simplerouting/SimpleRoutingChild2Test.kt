package com.badoo.ribs.samples.simplerouting

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.simplerouting.R
import com.badoo.ribs.samples.simplerouting.simple_routing_child2.SimpleRoutingChild2
import com.badoo.ribs.samples.simplerouting.simple_routing_child2.builder.SimpleRoutingChild2Builder
import org.junit.Rule
import org.junit.Test

class SimpleRoutingChild2Test {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        SimpleRoutingChild2Builder(
            object : SimpleRoutingChild2.Dependency {}
        ).build(root(savedInstanceState))

    @Test
    fun showsStaticText() {
        onView(withId(R.id.content_text))
            .check(matches(withText(R.string.child2_text)))
    }
}
