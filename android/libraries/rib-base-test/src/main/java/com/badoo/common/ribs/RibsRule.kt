package com.badoo.common.ribs


import android.os.Bundle
import androidx.test.rule.ActivityTestRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.Rib
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class RibsRule(
    builder: ((RibTestActivity, Bundle?) -> Rib)? = null
): ActivityTestRule<RibTestActivity>(
    RibTestActivity::class.java, true, builder != null
) {
    init {
        RibTestActivity.ribFactory = builder
    }

    fun start(ribFactory: ((RibTestActivity, Bundle?) -> Rib)) {
        RibTestActivity.ribFactory = ribFactory
        launchActivity(null)
    }

    override fun apply(base: Statement, description: Description?): Statement =
        super.apply(object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    RibTestActivity.ribFactory = null
                }
            }
        }, description)
}
