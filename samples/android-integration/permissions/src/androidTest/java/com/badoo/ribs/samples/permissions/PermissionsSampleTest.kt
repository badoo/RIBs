package com.badoo.ribs.samples.permissions

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.GrantPermissionRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.permissions.rib.PermissionsSampleBuilder
import com.badoo.ribs.samples.permissions.rib.PermissionsSample
import com.badoo.ribs.test.RibsRule
import org.junit.Rule
import org.junit.Test

class PermissionsSampleTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant()

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        PermissionsSampleBuilder(object : PermissionsSample.Dependency {
            override val permissionRequester: PermissionRequester =
                ribTestActivity.integrationPoint.permissionRequester
        }).build(BuildContext.root(savedInstanceState))

    @Test
    fun givenPermissionGrantedWhenRequestPermissionClickedThenChangedAvailablePermissionsText() {
        onView(withId(R.id.availablePermissions_text))
            .check(matches(withText("")))

        onView(withId(R.id.check_permissions_button)).perform(click())

        onView(withId(R.id.availablePermissions_text))
            .check(matches(withText(CAMERA_PERMISSION_GRANTED_TEXT)))
    }

    companion object {
        private const val CAMERA_PERMISSION_GRANTED_TEXT =
            "CheckPermissionsResult(granted=[android.permission.CAMERA], notGranted=[], shouldShowRationale=[])"
    }
}
