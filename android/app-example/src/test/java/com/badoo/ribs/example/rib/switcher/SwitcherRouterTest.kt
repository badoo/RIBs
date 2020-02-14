package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.DialogRoutingAction
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.blocker.BlockerBuilder
import com.badoo.ribs.example.rib.blocker.BlockerView
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import com.badoo.ribs.example.rib.foo_bar.FooBarBuilder
import com.badoo.ribs.example.rib.foo_bar.FooBarView
import com.badoo.ribs.example.rib.hello_world.HelloWorldBuilder
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.Menu.MenuItem
import com.badoo.ribs.example.rib.menu.MenuBuilder
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.example.rib.util.TestNode
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit tests that check only router
 * It may be helpful in case of complex routing logic
 */
class SwitcherRouterTest {

    private val fooBarNode = TestNode<FooBarView>()
    private val fooBarBuilder = mock<FooBarBuilder> { on { build(anyOrNull()) } doReturn fooBarNode }

    private val helloWorldNode = HelloWorldNode(null, mock(), mock(), mock())
    private val helloWorldBuilder = mock<HelloWorldBuilder> { on { build(anyOrNull()) } doReturn helloWorldNode }

    private val dialogExampleNode = TestNode<DialogExampleView>()
    private val dialogExampleBuilder = mock<DialogExampleBuilder> { on { build(anyOrNull()) } doReturn dialogExampleNode }

    private val blockerNode = TestNode<BlockerView>()
    private val blockerBuilder = mock<BlockerBuilder> { on { build(anyOrNull()) } doReturn blockerNode }

    private val menuNode = TestNode<MenuView>()
    private val menuBuilder = mock<MenuBuilder> { on { build(anyOrNull()) } doReturn menuNode }

    private val dialogLauncher: DialogLauncher = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val router = SwitcherRouter(
        savedInstanceState = null,
        transitionHandler = null,
        fooBarBuilder = fooBarBuilder,
        helloWorldBuilder = helloWorldBuilder,
        dialogExampleBuilder = dialogExampleBuilder,
        blockerBuilder = blockerBuilder,
        menuBuilder = menuBuilder,
        dialogLauncher = dialogLauncher,
        dialogToTestOverlay = dialogToTestOverlay
    )

    @Test
    fun `Permanent_Menu configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Permanent.Menu).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().node).isEqualTo(menuNode)
    }

    @Test
    fun `Permanent_Menu configuration resolves in Node with AttachMode PARENT`() {
        val routingAction = router.resolveConfiguration(Permanent.Menu).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().viewAttachMode).isEqualTo(Node.AttachMode.PARENT)
    }

    @Test
    fun `Content_Hello configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Content.Hello).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().node).isEqualTo(helloWorldNode)
    }

    @Test
    fun `Content_Hello configuration resolves in Node with AttachMode PARENT`() {
        val routingAction = router.resolveConfiguration(Content.Hello).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().viewAttachMode).isEqualTo(Node.AttachMode.PARENT)
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
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().node).isEqualTo(fooBarNode)
    }

    @Test
    fun `Content_Foo configuration resolves in Node with AttachMode PARENT`() {
        val routingAction = router.resolveConfiguration(Content.Foo).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().viewAttachMode).isEqualTo(Node.AttachMode.PARENT)
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
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().node).isEqualTo(dialogExampleNode)
    }

    @Test
    fun `Content_DialogsExample configuration resolves in Node with AttachMode PARENT`() {
        val routingAction = router.resolveConfiguration(Content.DialogsExample).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().viewAttachMode).isEqualTo(Node.AttachMode.PARENT)
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
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().node).isEqualTo(blockerNode)
    }

    @Test
    fun `Content_Blocker configuration resolves in Node with AttachMode PARENT`() {
        val routingAction = router.resolveConfiguration(Content.Blocker).apply { execute() }
        val nodes = routingAction.buildNodes(emptyList())

        assertThat(nodes).hasSize(1)
        assertThat(nodes.first().viewAttachMode).isEqualTo(Node.AttachMode.PARENT)
    }

    @Test
    fun `Overlay_Dialog configuration resolves to DialogRoutingAction`() {
        val routingAction = router.resolveConfiguration(Overlay.Dialog).apply { execute() }

        assertThat(routingAction).isInstanceOf(DialogRoutingAction::class.java)
    }
}
