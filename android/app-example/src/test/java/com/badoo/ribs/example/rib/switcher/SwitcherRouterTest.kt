package com.badoo.ribs.example.rib.switcher

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
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.Dialogs
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.example.rib.menu.MenuBuilder
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Blocker
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Foo
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Hello
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay.Dialog
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.example.rib.util.TestNode
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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

    private val rootNode = TestNode(router)

    @Test
    fun `attach - attaches menu and dialog example node`() {
        router.onAttach()
        router.onAttachView()

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, dialogExampleNode)
    }

    @Test
    fun `attach - publishes select dialog menu item event`() {
        val observer = router.menuUpdater.subscribeOnTestObserver()

        router.onAttach()
        router.onAttachView()

        observer.assertValue(Menu.Input.SelectMenuItem(Dialogs))
    }

    @Test
    fun `hello configuration - attaches hello world and menu nodes`() {
        router.onAttach()
        router.onAttachView()

        router.replace(Hello)

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, helloWorldNode)
    }

    @Test
    fun `hello configuration - publishes select hello world menu item event`() {
        router.onAttach()
        router.onAttachView()
        val observer = router.menuUpdater.subscribeOnTestObserver()

        router.replace(Hello)

        observer.assertValue(Menu.Input.SelectMenuItem(HelloWorld))
    }

    @Test
    fun `foo configuration - attaches foo bar and menu nodes`() {
        router.onAttach()
        router.onAttachView()

        router.replace(Foo)

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, fooBarNode)
    }

    @Test
    fun `foo configuration - publishes select foo bar menu item event`() {
        router.onAttach()
        router.onAttachView()
        val observer = router.menuUpdater.subscribeOnTestObserver()

        router.replace(Foo)

        observer.assertValue(Menu.Input.SelectMenuItem(FooBar))
    }

    @Test
    fun `overlay dialog configuration - shows overlay dialog`() {
        router.onAttach()
        router.onAttachView()

        router.pushOverlay(Dialog)

        verify(dialogLauncher).show(eq(dialogToTestOverlay), any())
    }

    @Test
    fun `blocker configuration - attaches blocker and menu nodes`() {
        router.onAttach()
        router.onAttachView()

        router.replace(Blocker)

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, blockerNode)
    }
}
