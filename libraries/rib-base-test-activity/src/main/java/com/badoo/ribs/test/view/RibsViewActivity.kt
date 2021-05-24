package com.badoo.ribs.test.view

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory

class RibsViewActivity : AppCompatActivity() {

    lateinit var view: RibView
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME?.also { setTheme(it) }
        super.onCreate(savedInstanceState)
        val rootViewHost = AndroidRibViewHost(findViewById(android.R.id.content))
        view = requireNotNull(VIEW_FACTORY).invoke(ViewFactory.Context(rootViewHost, lifecycle))
        setContentView(view.androidView)
    }

    companion object {
        @StyleRes
        var THEME: Int? = null
        var VIEW_FACTORY: ViewFactory<out RibView>? = null
    }

}
