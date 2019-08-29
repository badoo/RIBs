package com.badoo.ribs.example.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.core.routing.portal.PortalBuilder
import com.badoo.ribs.core.routing.portal.PortalNode
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherNode
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import com.badoo.ribs.example.util.CoffeeMachine
import com.badoo.ribs.example.util.StupidCoffeeMachine

/** The sample app's single activity */
class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    private lateinit var workflowRoot: Portal.Workflow

    override fun createRib(savedInstanceState: Bundle?): PortalNode =
        PortalBuilder(
            object : Portal.Dependency {
                override fun defaultRoutingAction(): (Portal.OtherSide) -> RoutingAction<Nothing> = { portal ->
                    attach { buildSwitcherNode(portal, it) }
                }

                private fun buildSwitcherNode(portal: Portal.OtherSide, savedInstanceState: Bundle?): SwitcherNode {
                    return SwitcherBuilder(
                        object : Switcher.Dependency {
                            override fun ribCustomisation(): RibCustomisationDirectory =
                                AppRibCustomisations

                            override fun activityStarter(): ActivityStarter = activityStarter
                            override fun permissionRequester(): PermissionRequester =
                                permissionRequester

                            override fun dialogLauncher(): DialogLauncher = this@RootActivity
                            override fun coffeeMachine(): CoffeeMachine = StupidCoffeeMachine()
                            override fun portal(): Portal.OtherSide = portal
                        }
                    ).build(savedInstanceState)
                }
            }

        ).build(savedInstanceState).also {
            workflowRoot = it
        }

//    override val workflowFactory: (Intent) -> Disposable? = {
//        when {
//            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://workflow1"
//            (it.data?.host == "workflow1") -> executeWorkflow1()
//
//            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://workflow2"
//            (it.data?.host == "workflow2") -> executeWorkflow2()
//
//            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://testcrash"
//            (it.data?.host == "testcrash") -> executeTestCrash()
//            else -> null
//        }
//    }
//
//    private fun executeWorkflow1(): Disposable? =
//        workflowRoot
//            .goToSwitcher()
//            .flatMap { it.attachHelloWorld() }
//            .subscribe { workflow: HelloWorld.Workflow ->
//                Log.d("WORKFLOW", "Success")
//            }
//
//    @SuppressWarnings("OptionalUnit")
//    private fun executeWorkflow2(): Disposable? =
//        workflowRoot
//            .goToSwitcher()
//            .flatMap {
//                Observable.combineLatest(
//                    it.doSomethingAndStayOnThisNode(),
//                    it.waitForHelloWorld()
//                        .flatMap { it.somethingSomethingDarkSide() },
//                    BiFunction<Switcher.Workflow, HelloWorld.Workflow, Unit> { _, _ -> Unit }
//            }
//            .toObservable()
//
//        ).subscribe {
//            Log.d("WORKFLOW", "Success")
//        }
//
//    private fun executeTestCrash(): Disposable? =
//        (rootNode as Switcher.Workflow)
//            .testCrash()
//            .subscribe { workflow: HelloWorld.Workflow ->
//                Log.d("WORKFLOW", "This should crash before here")
//            }
}
