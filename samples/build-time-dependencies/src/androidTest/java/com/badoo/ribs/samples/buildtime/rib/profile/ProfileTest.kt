package com.badoo.ribs.samples.buildtime.rib.profile

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.buildtime.R
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
