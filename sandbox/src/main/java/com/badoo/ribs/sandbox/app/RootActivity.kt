package com.badoo.ribs.sandbox.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.utils.debug.DebugControlsHost
import com.badoo.ribs.core.plugin.utils.debug.GrowthDirection
import com.badoo.ribs.core.plugin.utils.logger.Logger
import com.badoo.ribs.core.plugin.utils.memoryleak.LeakDetector
import com.badoo.ribs.debug.TreePrinter
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.PortalRouter
import com.badoo.ribs.portal.RxPortal
import com.badoo.ribs.portal.RxPortalBuilder
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.transition.handler.CrossFader
import com.badoo.ribs.routing.transition.handler.Slider
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx2.workflows.RxWorkflowActivity
import com.badoo.ribs.sandbox.BuildConfig
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.switcher.Switcher
import com.badoo.ribs.sandbox.rib.switcher.SwitcherBuilder
import com.badoo.ribs.sandbox.util.CoffeeMachine
import com.badoo.ribs.sandbox.util.StupidCoffeeMachine
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import leakcanary.AppWatcher

/** The sample app's single activity */
class RootActivity : RxWorkflowActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    private lateinit var workflowRoot: RxPortal

    override fun createRib(savedInstanceState: Bundle?) =
        RxPortalBuilder(
            object : Portal.Dependency {
                override val defaultResolution: (Portal.OtherSide) -> Resolution =
                    { portal ->
                        child { buildSwitcherNode(portal, it) }
                    }

                override val transitionHandler: TransitionHandler<PortalRouter.Configuration> =
                    TransitionHandler.multiple(
                        Slider { it.configuration is PortalRouter.Configuration.Content },
                        CrossFader { it.configuration is PortalRouter.Configuration.Overlay }
                    )

                private fun buildSwitcherNode(
                    portal: Portal.OtherSide,
                    buildContext: BuildContext
                ): Switcher {
                    return SwitcherBuilder(
                        object : Switcher.Dependency {
                            override val activityStarter: ActivityStarter =
                                integrationPoint.activityStarter
                            override val permissionRequester: PermissionRequester =
                                integrationPoint.permissionRequester
                            override val dialogLauncher: DialogLauncher =
                                integrationPoint.dialogLauncher
                            override val coffeeMachine: CoffeeMachine = StupidCoffeeMachine()
                            override val portal: Portal.OtherSide =
                                portal
                        }
                    ).build(buildContext)
                }
            }
        ).build(root(
            savedInstanceState = savedInstanceState,
            customisations = AppRibCustomisations,
            defaultPlugins = { node ->
                if (BuildConfig.DEBUG) {
                    listOfNotNull(
                        createLeakDetector(),
                        createLogger(),
                        if (node.isRoot) createDebugControlHost() else null
                    )
                } else emptyList()
            }
        )).also {
            workflowRoot = it
        }

    private fun createLogger(): Plugin = Logger<Switcher>(
        log = { rib, event ->
            Log.d("Rib Logger", "$rib: $event")
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

    override val workflowFactory: (Intent) -> Observable<*>? = {
        when {
            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://workflow1"
            (it.data?.host == "workflow1") -> executeWorkflow1()

            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://workflow2"
            (it.data?.host == "workflow2") -> executeWorkflow2()

            else -> null
        }
    }

    private fun executeWorkflow1(): Observable<*> =
        switcher()
            .flatMap { it.attachHelloWorld() }
            .toObservable()

    @SuppressWarnings("OptionalUnit")
    private fun executeWorkflow2(): Observable<*> =
        Observable.combineLatest(
            switcher()
                .flatMap { it.doSomethingAndStayOnThisNode() }
                .toObservable(),

            switcher()
                .flatMap { it.waitForHelloWorld() }
                .flatMap { it.somethingSomethingDarkSide() }
                .toObservable(),

            BiFunction<Switcher, HelloWorld, Unit> { _, _ -> Unit }
        )

    @Suppress("UNCHECKED_CAST")
    private fun switcher() =
        Single
            .just(workflowRoot)
            .flatMap { it.showDefault() as Single<Switcher> }
}
