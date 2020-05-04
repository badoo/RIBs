package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.DialogRoutingAction
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.blocker.BlockerBuilder
import com.badoo.ribs.example.rib.blocker.BlockerView
import com.badoo.ribs.example.rib.dialog_example.DialogExampleNode
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import com.badoo.ribs.example.rib.foo_bar.FooBarBuilder
import com.badoo.ribs.example.rib.foo_bar.FooBarNode
import com.badoo.ribs.example.rib.hello_world.HelloWorldBuilder
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.Menu.MenuItem
import com.badoo.ribs.example.rib.menu.MenuBuilder
import com.badoo.ribs.example.rib.menu.MenuNode
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.example.rib.util.TestNode
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit tests that check only router
 * It may be helpful in case of complex routing logic
 */
class SwitcherRouterTest {
    
    private val fooBarNode = FooBarNode(null, mock(), BuildParams.Empty(), emptySet())
    private val fooBarBuilder = mock<FooBarBuilder> { on { build(any()) } doReturn fooBarNode }

    private val helloWorldNode = HelloWorldNode(null, mock(), mock(), BuildParams.Empty())
    private val helloWorldBuilder = mock<HelloWorldBuilder> { on { build(any()) } doReturn helloWorldNode }

    private val dialogExampleNode = DialogExampleNode(BuildParams.Empty(), null, mock(), mock())
    private val dialogExampleBuilder = mock<DialogExampleBuilder> { on { build(any()) } doReturn dialogExampleNode }

    private val blockerNode = TestNode<BlockerView>()
    private val blockerBuilder = mock<BlockerBuilder> { on { build(any()) } doReturn blockerNode }

    private val menuNode = MenuNode(BuildParams.Empty(), mock(), mock())
    private val menuBuilder = mock<MenuBuilder> { on { build(any()) } doReturn menuNode }

    private val dialogLauncher: DialogLauncher = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val router = SwitcherRouter(
        BuildParams.Empty(),
        transitionHandler = null,
        fooBarBuilder = fooBarBuilder,
        helloWorldBuilder = helloWorldBuilder,
        dialogExampleBuilder = dialogExampleBuilder,
        blockerBuilder = blockerBuilder,
        menuBuilder = menuBuilder,
        dialogLauncher = dialogLauncher,
        dialogToTestOverlay = dialogToTestOverlay
    )

    private val rootNode = TestNode(router = router)

    @Test
    fun `Permanent_Menu configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Permanent.Menu).apply { execute() }
        val nodes = routingAction.buildNodes(listOf(root(null)))

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first()).isEqualTo(menuNode)
    }

    @Test
    fun `Content_Hello configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Content.Hello).apply { execute() }
        val nodes = routingAction.buildNodes(listOf(root(null)))

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first()).isEqualTo(helloWorldNode)
    }

    @Test
    fun `Content_Hello configuration triggers menu update with correct MenuItem`() {
        val observer = router.menuUpdater.subscribeOnTestObserver()
        val routingAction = router.resolveConfiguration(Content.Hello)
        routingAction.execute()

        observer.assertValue(Menu.Input.SelectMenuItem(MenuItem.HelloWorld))
    }

    @Test
    fun `Content_Foo configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Content.Foo).apply { execute() }
        val nodes = routingAction.buildNodes(listOf(root(null)))

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first()).isEqualTo(fooBarNode)
    }

    @Test
    fun `Content_Foo configuration triggers menu update with correct MenuItem`() {
        val observer = router.menuUpdater.subscribeOnTestObserver()
        val routingAction = router.resolveConfiguration(Content.Foo)
        routingAction.execute()

        observer.assertValue(Menu.Input.SelectMenuItem(MenuItem.FooBar))
    }

    @Test
    fun `Content_DialogsExample configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Content.DialogsExample).apply { execute() }
        val nodes = routingAction.buildNodes(listOf(root(null)))

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first()).isEqualTo(dialogExampleNode)
    }

    @Test
    fun `Content_DialogsExample configuration triggers menu update with correct MenuItem`() {
        val observer = router.menuUpdater.subscribeOnTestObserver()
        val routingAction = router.resolveConfiguration(Content.DialogsExample)
        routingAction.execute()

        observer.assertValue(Menu.Input.SelectMenuItem(MenuItem.Dialogs))
    }

    @Test
    fun `Content_Blocker configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Content.Blocker).apply { execute() }
        val nodes = routingAction.buildNodes(listOf(root(null)))

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first()).isEqualTo(blockerNode)
    }

    @Test
    fun `Overlay_Dialog configuration resolves to DialogRoutingAction`() {
        val routingAction = router.resolveConfiguration(Overlay.Dialog).apply { execute() }

        assertThat(routingAction).isInstanceOf(DialogRoutingAction::class.java)
    }
}
