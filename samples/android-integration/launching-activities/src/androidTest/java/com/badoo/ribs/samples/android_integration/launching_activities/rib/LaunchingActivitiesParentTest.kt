package com.badoo.ribs.samples.android_integration.launching_activities.rib

import android.content.ComponentName
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.android_integration.launching_activities.R
import com.badoo.ribs.samples.android_integration.launching_activities.app.OtherActivity
import com.badoo.ribs.samples.android_integration.launching_activities.rib.child1.LaunchingActivitiesChild1Builder
import com.badoo.ribs.samples.android_integration.launching_activities.rib.child_common.LaunchingActivitiesChildBase
import com.badoo.ribs.test.RibsRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LaunchingActivitiesParentTest {
    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState ->
        buildRib(activity, savedInstanceState)
    }

    @Before
    fun setUp() {
        init()
    }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        LaunchingActivitiesChild1Builder(object : LaunchingActivitiesChildBase.Dependency {
            override val activityStarter: ActivityStarter = ribTestActivity.integrationPoint.activityStarter
        }).build(BuildContext.root(savedInstanceState))

    @Test
    fun whenLaunchActivityButtonIsPressedThenOtherActivityIsStarted() {
        onView(ViewMatchers.withId(R.id.launchActivityButton)).perform(click())
        intended(hasComponent(ComponentName(InstrumentationRegistry.getInstrumentation().context, OtherActivity::class.java)))
    }
}
