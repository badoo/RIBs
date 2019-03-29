package com.badoo.ribs.example.rib.switcher

import android.os.Parcelable
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.CompositeRoutingAction.Companion.composite
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.showDialog
import com.badoo.ribs.core.routing.action.InvokeOnExecute.Companion.execute
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.builder.BlockerBuilder
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import com.badoo.ribs.example.rib.foo_bar.builder.FooBarBuilder
import com.badoo.ribs.example.rib.hello_world.builder.HelloWorldBuilder
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.example.rib.menu.Menu.MenuItem
import com.badoo.ribs.example.rib.menu.builder.MenuBuilder
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import com.jakewharton.rxrelay2.PublishRelay
import kotlinx.android.parcel.Parcelize

class SwitcherRouter(
    private val fooBarBuilder: FooBarBuilder,
    private val helloWorldBuilder: HelloWorldBuilder,
    private val dialogExampleBuilder: DialogExampleBuilder,
    private val blockerBuilder: BlockerBuilder,
    private val menuBuilder: MenuBuilder,
    private val dialogLauncher: DialogLauncher,
    private val dialogToTestOverlay: DialogToTestOverlay
    ): Router<Configuration, SwitcherView>(
    initialConfiguration = Configuration.DialogsExample
) {
    internal val menuUpdater = PublishRelay.create<Menu.Input>()

    override val permanentParts: List<() -> Node<*>> = listOf(
        { menuBuilder.build() }
    )

    sealed class Configuration : Parcelable {
        @Parcelize object Hello : Configuration()
        @Parcelize object Foo : Configuration()
        @Parcelize object DialogsExample : Configuration()
        @Parcelize object OverlayDialog : Configuration(), Overlay
        @Parcelize object Blocker : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<SwitcherView> =
        when (configuration) {
            is Configuration.Hello -> composite(
                attach { helloWorldBuilder.build() },
                execute { menuUpdater.accept(SelectMenuItem(MenuItem.HelloWorld)) }
            )
            is Configuration.Foo -> composite(
                attach { fooBarBuilder.build() },
                execute { menuUpdater.accept(SelectMenuItem(MenuItem.FooBar)) }
            )
            is Configuration.DialogsExample -> composite(
                attach { dialogExampleBuilder.build() },
                execute { menuUpdater.accept(SelectMenuItem(MenuItem.Dialogs)) }
            )
            is Configuration.OverlayDialog -> showDialog(dialogLauncher, dialogToTestOverlay)
            is Configuration.Blocker -> attach { blockerBuilder.build() }
        }

    override fun getParentViewForChild(child: Rib, view: SwitcherView?): ViewGroup? =
        when (child) {
            is Menu -> view!!.menuContainer
            is Blocker -> view!!.blockerContainer
            else -> view!!.contentContainer
        }
}
