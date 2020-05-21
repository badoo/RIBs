package com.badoo.ribs.sandbox.rib.hello_world

import android.os.Bundle
import android.os.Parcelable
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.sandbox.R
import org.junit.Rule
import org.junit.Test

class HelloWorldTestDeferred {

    @get:Rule
    val ribsRule = RibsRule()

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        HelloWorldBuilder(object : HelloWorld.Dependency {
            override fun portal(): Portal.OtherSide = Portal.OtherSide.NOOP
            override fun activityStarter(): ActivityStarter = ribTestActivity.activityStarter
        }).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
        ribsRule.start { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

        onView(withId(R.id.hello_title)).check(matches(isDisplayed()))
    }
}
