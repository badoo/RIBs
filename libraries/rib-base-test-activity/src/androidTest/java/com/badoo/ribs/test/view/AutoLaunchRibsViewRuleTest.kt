package com.badoo.ribs.test.view

import com.badoo.ribs.test.activity.R
import com.badoo.ribs.test.sample.SampleViewImpl
import org.junit.Rule

class AutoLaunchRibsViewRuleTest : RibsViewRuleTestBase(invokeStart = false) {

    @get:Rule
    override val rule = RibsViewRule(
        launchActivity = true,
        theme = R.style.Theme_AppCompat_Dialog,
        viewFactory = { SampleViewImpl(it) }
    )

}
