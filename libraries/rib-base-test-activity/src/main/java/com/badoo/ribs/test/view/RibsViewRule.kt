package com.badoo.ribs.test.view

import androidx.annotation.StyleRes
import androidx.test.rule.ActivityTestRule
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class RibsViewRule<View : RibView>(
    private val launchActivity: Boolean = true,
    @StyleRes private val theme: Int? = null,
    private var viewFactory: ViewFactory<View>,
) : ActivityTestRule<RibsViewActivity>(RibsViewActivity::class.java, true, launchActivity) {

    @Suppress("UNCHECKED_CAST")
    val view: View
        get() = activity.view as View

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
        RibsViewActivity.THEME = theme
        RibsViewActivity.VIEW_FACTORY = viewFactory
    }

    private fun reset() {
        RibsViewActivity.THEME = null
        RibsViewActivity.VIEW_FACTORY = null
    }

    fun start() {
        require(!launchActivity) {
            "Activity will be launched automatically, launchActivity parameter was passed into constructor"
        }
        launchActivity(null)
    }

}
