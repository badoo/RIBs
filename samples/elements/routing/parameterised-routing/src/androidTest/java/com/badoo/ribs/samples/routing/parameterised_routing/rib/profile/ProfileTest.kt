package com.badoo.ribs.samples.routing.parameterised_routing.rib.profile

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.routing.parameterised_routing.R
import org.junit.Rule
import org.junit.Test

class ProfileTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        ProfileBuilder().build(root(savedInstanceState), Profile.Params(1))

    @Test
    fun verifyProfileText() {
        onView(withId(R.id.profile_label))
            .check(matches(withText("This is the profile RIB built for user: 1")))
    }
}
