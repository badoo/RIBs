package com.badoo.ribs.test.view

import androidx.annotation.StyleRes
import androidx.test.rule.ActivityTestRule
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory

open class RibViewTestRule<View : RibView>(
    private val launchActivity: Boolean = true,
    @StyleRes private val theme: Int? = null,
    private var viewFactory: ViewFactory<View>,
) : ActivityTestRule<RibViewTestActivity>(RibViewTestActivity::class.java, true, launchActivity) {

    private lateinit var rootViewHost: AndroidRibViewHost
    lateinit var view: View
        private set

    override fun afterActivityLaunched() {
        RibViewTestActivity.THEME = theme
        runOnUiThread {
            theme?.also { activity.setTheme(it) }
            rootViewHost = AndroidRibViewHost(activity.rootView)
            view = viewFactory.invoke(ViewFactory.Context(rootViewHost, activity.lifecycle))
            activity.setContentView(view.androidView)
        }
    }

    override fun afterActivityFinished() {
        RibViewTestActivity.THEME = null
    }

    fun start() {
        require(!launchActivity) {
            "Activity will be launched automatically, launchActivity parameter was passed into constructor"
        }
        launchActivity(null)
    }

}
