package com.badoo.ribs.example.rib.lorem_ipsum

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.R
import com.badoo.ribs.example.app.AppRibCustomisations
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class LoremIpsumTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        LoremIpsumBuilder(object : LoremIpsum.Dependency {
            override fun ribCustomisation(): Directory = AppRibCustomisations
            override fun activityStarter(): ActivityStarter = ribTestActivity.activityStarter
            override fun permissionRequester(): PermissionRequester = ribTestActivity.permissionRequester
            override fun dialogLauncher(): DialogLauncher = ribTestActivity
            override fun loremIpsumOutput(): Consumer<LoremIpsum.Output> = Consumer {}
        }).build()

    @Test
    fun testTextDisplayed() {
         onView(withId(R.id.lorem_ipsum_text)).check(matches(isDisplayed()))
    }
}
