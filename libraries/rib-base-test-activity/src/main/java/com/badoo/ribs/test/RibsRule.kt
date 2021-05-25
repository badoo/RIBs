package com.badoo.ribs.test

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.test.rule.ActivityTestRule
import com.badoo.ribs.core.Rib
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class RibsRule<T : Rib>(
    @StyleRes private val theme: Int? = null,
    private var builder: ((RibTestActivity, Bundle?) -> T)? = null
) : ActivityTestRule<RibTestActivity>(
    RibTestActivity::class.java, true, builder != null
) {

    @Suppress("UNCHECKED_CAST")
    val rib: T
        get() = activity.rib as T

    override fun apply(base: Statement, description: Description): Statement {
        val activityStatement = super.apply(base, description)
        return object : Statement() {
            override fun evaluate() {
                try {
                    setup()
                    activityStatement.evaluate()
                } finally {
                    reset()
                }
            }
        }
    }

    private fun setup() {
        RibTestActivity.RIB_FACTORY = builder
        RibTestActivity.THEME = theme
    }

    private fun reset() {
        RibTestActivity.RIB_FACTORY = null
        RibTestActivity.THEME = null
    }

    fun start(ribFactory: ((RibTestActivity, Bundle?) -> T)) {
        builder = ribFactory
        setup()
        launchActivity(null)
    }

}
