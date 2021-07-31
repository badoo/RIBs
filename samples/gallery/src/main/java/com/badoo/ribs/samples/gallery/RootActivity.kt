package com.badoo.ribs.samples.gallery

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.utils.debug.DebugControlsHost
import com.badoo.ribs.core.plugin.utils.debug.GrowthDirection
import com.badoo.ribs.core.plugin.utils.logger.Logger
import com.badoo.ribs.core.plugin.utils.memoryleak.LeakDetector
import com.badoo.ribs.debug.TreePrinter
import com.badoo.ribs.samples.gallery.rib.root.container.RootContainer
import com.badoo.ribs.samples.gallery.rib.root.container.RootContainerBuilder
import leakcanary.AppWatcher

class RootActivity : RibActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        RootContainerBuilder(
            object : RootContainer.Dependency {
                override val activityStarter: ActivityStarter
                    get() = integrationPoint.activityStarter

                override val permissionRequester: PermissionRequester
                    get() = integrationPoint.permissionRequester

                override val dialogLauncher: DialogLauncher
                    get() = integrationPoint.dialogLauncher
            }
        ).build(root(
            savedInstanceState = savedInstanceState,
            defaultPlugins = { node ->
                if (BuildConfig.DEBUG) {
                    listOfNotNull(
                        createLeakDetector(),
                        createLogger(),
                        if (node.isRoot) createDebugControlHost() else null
                    )
                } else emptyList()
            }
        ))

    private fun createLogger(): Plugin = Logger(
        log = { rib, event ->
            Log.d("Gallery logger", "$rib: $event")
        }
    )

    private fun createLeakDetector(): Plugin = LeakDetector(
        watcher = { obj, msg ->
            AppWatcher.objectWatcher.watch(obj, msg)
        }
    )

    private fun createDebugControlHost(): Plugin =
        DebugControlsHost(
            viewGroupForChildren = { findViewById(R.id.debug_controls_host) },
            growthDirection = GrowthDirection.BOTTOM,
            defaultTreePrinterFormat = TreePrinter.FORMAT_SIMPLE
        )
}
