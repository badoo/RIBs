package com.badoo.ribs.example.app

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.recyclerview.RecyclerViewHost
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Dependency.LayoutManagerFactory
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.EAGER
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.LAZY
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input.Add
import com.badoo.ribs.android.recyclerview.RecyclerViewHostBuilder
import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.core.routing.portal.PortalBuilder
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.builder.FooBarBuilder
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherNode
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import com.badoo.ribs.example.util.CoffeeMachine
import com.badoo.ribs.example.util.StupidCoffeeMachine
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer

/** The sample app's single activity */
class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    private lateinit var workflowRoot: Portal.Workflow

    private val portalBuilder = PortalBuilder(
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
                ).build(savedInstanceState).also {
//                    workflowRoot = it
                }
            }
        }
    )

    private val fooBarBuilder = FooBarBuilder(object : FooBar.Dependency {
        override fun foobarInput(): ObservableSource<FooBar.Input> = empty()
        override fun foobarOutput(): Consumer<FooBar.Output> = Consumer {}
        override fun permissionRequester(): PermissionRequester = this@RootActivity.permissionRequester
        override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
    })

    private val loremIpsumBuilder = LoremIpsumBuilder(object : LoremIpsum.Dependency {
        override fun loremIpsumOutput(): Consumer<LoremIpsum.Output> = Consumer { }
        override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
    })

    private val ribPagerImpl = RibResolver(
        fooBarBuilder = fooBarBuilder,
        loremIpsumBuilder = loremIpsumBuilder
    )

    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        RecyclerViewHostBuilder(
            object : RecyclerViewHost.Dependency<Item> {
                override fun layoutManagerFactory(): LayoutManagerFactory = LayoutManagerFactory.linear
                override fun resolver(): RecyclerViewRibResolver<Item> = ribPagerImpl
                override fun initialElements(): List<Item> = listOf(Item.FooBarItem)
                override fun recyclerViewHostInput(): ObservableSource<RecyclerViewHost.Input<Item>> =
                    just(Add<Item>(Item.LoremIpsumItem))
                override fun hostingStrategy(): RecyclerViewHost.HostingStrategy = LAZY
            }
        ).build(savedInstanceState)


    override val workflowFactory: (Intent) -> Observable<*>? = {
        when {
            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://workflow1"
            (it.data?.host == "workflow1") -> executeWorkflow1()

            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://workflow2"
            (it.data?.host == "workflow2") -> executeWorkflow2()

            // adb shell am start -a "android.intent.action.VIEW" -d "app-example://testcrash"
            (it.data?.host == "testcrash") -> executeTestCrash()
            else -> null
        }
    }

    private fun executeWorkflow1(): Observable<*> =
        switcher()
            .flatMap { it.attachHelloWorld()}
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

            BiFunction<Switcher.Workflow, HelloWorld.Workflow, Unit> { _, _ -> Unit }
        )

    private fun executeTestCrash(): Observable<*> =
        switcher()
            .flatMap { it.testCrash() }
            .toObservable()

    @Suppress("UNCHECKED_CAST")
    private fun switcher() =
        Single
            .just(workflowRoot)
            .flatMap { it.showDefault() as Single<Switcher.Workflow> }
}
