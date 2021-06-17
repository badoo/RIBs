package com.badoo.ribs.test

import com.badoo.ribs.test.activity.R
import com.badoo.ribs.test.sample.SampleRib
import org.junit.Rule

class ManualLaunchRibsRuleTest : RibsRuleTestBase(invokeStart = true) {

    @get:Rule
    override val rule = RibsRule<SampleRib>(
        theme = R.style.Theme_AppCompat_Dialog,
    )

}
