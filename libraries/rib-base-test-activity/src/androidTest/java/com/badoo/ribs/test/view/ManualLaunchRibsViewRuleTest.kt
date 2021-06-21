package com.badoo.ribs.test.view

import com.badoo.ribs.test.activity.R
import com.badoo.ribs.test.sample.SampleViewImpl
import org.junit.Rule

class ManualLaunchRibsViewRuleTest : RibsViewRuleTestBase(invokeStart = true) {

    @get:Rule
    override val rule = RibsViewRule(
        launchActivity = false,
        theme = R.style.Theme_AppCompat_Dialog,
        viewFactory = { SampleViewImpl(it) }
    )

}
