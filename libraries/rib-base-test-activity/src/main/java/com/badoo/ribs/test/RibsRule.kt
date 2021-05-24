package com.badoo.ribs.test

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.test.rule.ActivityTestRule
import com.badoo.ribs.core.Rib

open class RibsRule(
    @StyleRes private val theme: Int? = null,
    private var builder: ((RibTestActivity, Bundle?) -> Rib)? = null
) : ActivityTestRule<RibTestActivity>(
    RibTestActivity::class.java, true, builder != null
) {

    override fun beforeActivityLaunched() {
        RibTestActivity.ribFactory = builder
        RibTestActivity.THEME = theme
    }

    override fun afterActivityLaunched() {
        RibTestActivity.ribFactory = null
        RibTestActivity.THEME = null
    }

    fun start(ribFactory: ((RibTestActivity, Bundle?) -> Rib)) {
        builder = ribFactory
        launchActivity(null)
    }

}
