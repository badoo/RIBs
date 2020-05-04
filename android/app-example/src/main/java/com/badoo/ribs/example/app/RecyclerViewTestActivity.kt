package com.badoo.ribs.example.app

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.recyclerview.LayoutManagerFactory
import com.badoo.ribs.android.recyclerview.RecyclerViewFactory
import com.badoo.ribs.android.recyclerview.RecyclerViewHost
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.EAGER
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.android.recyclerview.RecyclerViewHostBuilder
import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.action.AddToRecyclerViewRoutingAction.Companion.recyclerView
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarBuilder
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumBuilder
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import com.badoo.ribs.example.util.CoffeeMachine
import com.badoo.ribs.example.util.StupidCoffeeMachine
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import kotlinx.android.parcel.Parcelize

/** The sample app's single activity */
class RecyclerViewTestActivity : RibActivity() {

    // We'll put these into the RecyclerView by resolving them to builders (see below)
    sealed class Item : Parcelable {
        @Parcelize object LoremIpsumItem : Item()
        @Parcelize object FooBarItem : Item()
        @Parcelize object Switcher : Item()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    private val fooBarBuilder = FooBarBuilder(object : FooBar.Dependency {
        override fun foobarInput(): ObservableSource<FooBar.Input> = Observable.empty()
        override fun foobarOutput(): Consumer<FooBar.Output> = Consumer {}
        override fun permissionRequester(): PermissionRequester = this@RecyclerViewTestActivity.permissionRequester
        override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
    })

    private val loremIpsumBuilder = LoremIpsumBuilder(object : LoremIpsum.Dependency {
        override fun loremIpsumOutput(): Consumer<LoremIpsum.Output> = Consumer { }
        override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
    })

    private val noopPortal = object : Portal.OtherSide {
        override fun showContent(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable) {
            // Sorry, no-op
        }

        override fun showOverlay(remoteRouter: Router<*, *, *, *, *>, remoteConfiguration: Parcelable) {
            // Sorry, no-op
        }
    }

    private val switcherBuilder = SwitcherBuilder(
        object : Switcher.Dependency {
            override fun ribCustomisation(): RibCustomisationDirectory =
                AppRibCustomisations

            override fun activityStarter(): ActivityStarter = activityStarter
            override fun permissionRequester(): PermissionRequester =
                permissionRequester

            override fun dialogLauncher(): DialogLauncher = this@RecyclerViewTestActivity
            override fun coffeeMachine(): CoffeeMachine = StupidCoffeeMachine()
            override fun portal(): Portal.OtherSide = noopPortal
        }
    )

    private val ribResolver = object : RecyclerViewRibResolver<Item> {
        override fun resolve(element: Item): RoutingAction =
            when (element) {
                Item.LoremIpsumItem -> recyclerView { loremIpsumBuilder.build(it) }
                Item.FooBarItem -> recyclerView { fooBarBuilder.build(it) }
                Item.Switcher-> recyclerView { switcherBuilder.build(it) }
            }
    }

    private val initialElements = listOf(
        Item.FooBarItem
    )

    private val inputCommands = Observable.just<Input<Item>>(
        Input.Add(Item.LoremIpsumItem),
        Input.Add(Item.Switcher)
    )

    override fun createRib(savedInstanceState: Bundle?): Rib =
        RecyclerViewHostBuilder(
            object : RecyclerViewHost.Dependency<Item> {
                override fun hostingStrategy(): RecyclerViewHost.HostingStrategy = EAGER
                override fun initialElements(): List<Item> = initialElements
                override fun recyclerViewHostInput(): ObservableSource<Input<Item>> = inputCommands
                override fun resolver(): RecyclerViewRibResolver<Item> = ribResolver
                override fun recyclerViewFactory(): RecyclerViewFactory = ::RecyclerView
                override fun layoutManagerFactory(): LayoutManagerFactory = ::LinearLayoutManager
                override fun viewHolderLayoutParams(): FrameLayout.LayoutParams =
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
            }
        ).build(BuildContext.root(savedInstanceState))
}
