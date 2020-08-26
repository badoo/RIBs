package com.badoo.ribs.sandbox.app

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.recyclerview.LayoutManagerFactory
import com.badoo.ribs.android.recyclerview.RecyclerViewFactory
import com.badoo.ribs.android.recyclerview.RecyclerViewHost
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.EAGER
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input.Add
import com.badoo.ribs.android.recyclerview.RecyclerViewHostBuilder
import com.badoo.ribs.android.recyclerview.routing.action.RecyclerViewItemResolution.Companion.recyclerView
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.Resolution
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarBuilder
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsumBuilder
import com.badoo.ribs.sandbox.rib.switcher.Switcher
import com.badoo.ribs.sandbox.rib.switcher.SwitcherBuilder
import com.badoo.ribs.sandbox.util.CoffeeMachine
import com.badoo.ribs.sandbox.util.StupidCoffeeMachine
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
        override fun permissionRequester(): PermissionRequester = this@RecyclerViewTestActivity.permissionRequester
    })

    private val loremIpsumBuilder = LoremIpsumBuilder(object : LoremIpsum.Dependency {})

    private val noopPortal = object : com.badoo.ribs.portal.Portal.OtherSide {
        override fun showContent(remoteNode: Node<*>, remoteConfiguration: Parcelable) {
            // Sorry, no-op
        }

        override fun showOverlay(remoteNode: Node<*>, remoteConfiguration: Parcelable) {
            // Sorry, no-op
        }
    }

    private val switcherBuilder =
        SwitcherBuilder(
            object : Switcher.Dependency {
                override fun activityStarter(): ActivityStarter = activityStarter
                override fun permissionRequester(): PermissionRequester = permissionRequester
                override fun dialogLauncher(): DialogLauncher = this@RecyclerViewTestActivity
                override fun coffeeMachine(): CoffeeMachine = StupidCoffeeMachine()
                override fun portal(): com.badoo.ribs.portal.Portal.OtherSide = noopPortal
            }
        )

    private val resolver = object :
        RoutingResolver<Item> {
        override fun resolve(routing: Routing<Item>): Resolution =
            when (routing.configuration) {
                Item.LoremIpsumItem -> recyclerView { loremIpsumBuilder.build(it) }
                Item.FooBarItem -> recyclerView { fooBarBuilder.build(it) }
                Item.Switcher-> recyclerView { switcherBuilder.build(it) }
            }
    }

    private val initialElements = listOf<Item>(
        Item.FooBarItem
    )

    private lateinit var recyclerViewHost: RecyclerViewHost<Item>

    override fun createRib(savedInstanceState: Bundle?): Rib =
        RecyclerViewHostBuilder(
            object : RecyclerViewHost.Dependency<Item> {
                override fun hostingStrategy(): RecyclerViewHost.HostingStrategy = EAGER
                override fun initialElements(): List<Item> = initialElements
                override fun resolver(): RoutingResolver<Item> = resolver
                override fun recyclerViewFactory(): RecyclerViewFactory = ::RecyclerView
                override fun layoutManagerFactory(): LayoutManagerFactory = ::LinearLayoutManager
                override fun viewHolderLayoutParams(): FrameLayout.LayoutParams =
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
            }
        ).build(root(savedInstanceState)).also {
            recyclerViewHost = it
        }

    override fun onResume() {
        super.onResume()
        recyclerViewHost.input.accept(Add(Item.LoremIpsumItem))
    }
}
