package com.samples.plugins.plugins_demo

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.samples.plugins.R

interface PluginsDemoView : RibView {

    interface Factory : ViewFactory<Nothing?, PluginsDemoView>

}


class PluginsDemoViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    PluginsDemoView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_plugins_demo
    ) : PluginsDemoView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> PluginsDemoView = {
            PluginsDemoViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
