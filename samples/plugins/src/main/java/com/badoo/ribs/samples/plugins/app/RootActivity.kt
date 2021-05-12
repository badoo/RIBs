package com.badoo.ribs.samples.plugins.app

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.utils.logger.Logger
import com.badoo.ribs.core.plugin.utils.memoryleak.LeakDetector
import com.badoo.ribs.samples.plugins.BuildConfig
import com.badoo.ribs.samples.plugins.R
import com.badoo.ribs.samples.plugins.plugins_demo.PluginsDemo
import com.badoo.ribs.samples.plugins.plugins_demo.PluginsDemoBuilder
import leakcanary.AppWatcher

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        return PluginsDemoBuilder(object : PluginsDemo.Dependency {

        }).build(root(
            savedInstanceState = savedInstanceState,
            defaultPlugins = {
                if (BuildConfig.DEBUG) {
                    listOfNotNull(
                        createLeakDetector(),
                        createLogger()
                    )
                } else emptyList()
            }
        ))
    }

    private fun createLogger(): Plugin = Logger(
        log = { rib, event ->
            Log.d("SampleLogger", "$rib: $event")
        }
    )

    private fun createLeakDetector(): Plugin = LeakDetector(
        watcher = { obj, msg ->
            AppWatcher.objectWatcher.watch(obj, msg)
        }
    )

}
