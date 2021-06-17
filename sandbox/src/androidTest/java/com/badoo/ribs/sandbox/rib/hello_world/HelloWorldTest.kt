package com.badoo.ribs.sandbox.rib.hello_world

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.RibTestActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.sandbox.R
import org.junit.Rule
import org.junit.Test

class HelloWorldTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        HelloWorldBuilder(object : HelloWorld.Dependency {
            override val portal: Portal.OtherSide = Portal.OtherSide.NOOP
            override val activityStarter: ActivityStarter = ribTestActivity.integrationPoint.activityStarter
        }).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
        onView(withId(R.id.hello_title)).check(matches(isDisplayed()))
    }
}
