package com.badoo.ribs.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.root.Root
import com.badoo.ribs.example.rib.root.RootNode
import com.badoo.ribs.example.rib.root.builder.RootBuilder
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherNode
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import com.badoo.ribs.example.util.CoffeeMachine
import com.badoo.ribs.example.util.StupidCoffeeMachine
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

/** The sample app's single activity */
class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    private lateinit var workflowRoot: Root.Workflow

    override fun createRib(savedInstanceState: Bundle?): RootNode =
        RootBuilder(
            object : Root.Dependency {
                override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
                override fun activityStarter(): ActivityStarter = activityStarter
                override fun permissionRequester(): PermissionRequester = permissionRequester
                override fun dialogLauncher(): DialogLauncher = this@RootActivity
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
