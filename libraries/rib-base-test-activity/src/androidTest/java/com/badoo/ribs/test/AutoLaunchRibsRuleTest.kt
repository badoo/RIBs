package com.badoo.ribs.test

import com.badoo.ribs.test.activity.R
import org.junit.Rule

class AutoLaunchRibsRuleTest : RibsRuleTestBase(invokeStart = false) {

    @get:Rule
    override val rule = RibsRule(
        theme = R.style.Theme_AppCompat_Dialog,
        builder = this.builder
    )

}
