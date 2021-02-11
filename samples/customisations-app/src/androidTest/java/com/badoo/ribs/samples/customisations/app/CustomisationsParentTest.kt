package com.badoo.ribs.samples.customisations.app

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.simplerouting.rib.simple_routing_parent.builder.SimpleRoutingParentBuilder
import org.junit.Rule
import org.junit.Test

class CustomisationsParentTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        SimpleRoutingParentBuilder(
            object : SimpleRoutingParent.Dependency {}
        ).build(
            root(
                savedInstanceState = savedInstanceState,
                customisations = AppCustomisations
            )
        )

    @Test
    fun whenOverriddenCustomisationThenVerifyCorrectParentTitle() {
        onView(withId(R.id.parent_title))
            .check(matches(withText(R.string.customisations_parent_text)))
    }

    @Test
    fun whenOverriddenCustomisationThenVerifyCorrectChild1Child2Title() {
        onView(withId(R.id.child1_child2_title))
            .check(matches(withText(R.string.customisations_child1_child2_text)))
    }

    @Test
    fun whenOverriddenCustomisationThenVerifyCorrectChild1Child1Title() {
        onView(withId(R.id.child1_child1_title))
            .check(matches(withText(R.string.customisations_child1_child1_text)))
    }
}
