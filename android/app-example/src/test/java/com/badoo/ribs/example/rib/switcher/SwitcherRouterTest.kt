package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.blocker.builder.BlockerBuilder
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import com.badoo.ribs.example.rib.foo_bar.builder.FooBarBuilder
import com.badoo.ribs.example.rib.hello_world.builder.HelloWorldBuilder
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.Dialogs
import com.badoo.ribs.example.rib.menu.builder.MenuBuilder
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Foo
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Hello
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Blocker
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay.Dialog
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.example.rib.util.TestNode
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.any
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

    private val fooBarBuilder = createBuilder<FooBarBuilder> { build() }
    private val fooBarNode = fooBarBuilder.build()

    private val helloWorldBuilder = createBuilder<HelloWorldBuilder> { build() }
    private val helloWorldNode = helloWorldBuilder.build()

    private val dialogExampleBuilder = createBuilder<DialogExampleBuilder> { build() }
    private val dialogExampleNode = dialogExampleBuilder.build()

    private val blockerBuilder = createBuilder<BlockerBuilder> { build() }
    private val blockerNode = blockerBuilder.build()

    private val menuBuilder = createBuilder<MenuBuilder> { build() }
    private val menuNode = menuBuilder.build()

    private val dialogLauncher: DialogLauncher = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val router = SwitcherRouter(
        fooBarBuilder,
        helloWorldBuilder,
        dialogExampleBuilder,
        blockerBuilder,
        menuBuilder,
        dialogLauncher,
        dialogToTestOverlay
    )

    private val rootNode = TestNode(router)

    @Test
    fun `attach - attaches menu and dialog example node`() {
        router.onAttach(null)
        router.onAttachView()

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, dialogExampleNode)
    }

    @Test
    fun `attach - publishes select dialog menu item event`() {
        val observer = router.menuUpdater.subscribeOnTestObserver()

        router.onAttach(null)
        router.onAttachView()

        observer.assertValue(Menu.Input.SelectMenuItem(Dialogs))
    }

    @Test
    fun `hello configuration - attaches hello world and menu nodes`() {
        router.onAttach(null)
        router.onAttachView()

        router.replace(Hello)

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, helloWorldNode)
    }

    @Test
    fun `hello configuration - publishes select hello world menu item event`() {
        router.onAttach(null)
        router.onAttachView()
        val observer = router.menuUpdater.subscribeOnTestObserver()

        router.replace(Hello)

        observer.assertValue(Menu.Input.SelectMenuItem(HelloWorld))
    }

    @Test
    fun `foo configuration - attaches foo bar and menu nodes`() {
        router.onAttach(null)
        router.onAttachView()

        router.replace(Foo)

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, fooBarNode)
    }

    @Test
    fun `foo configuration - publishes select foo bar menu item event`() {
        router.onAttach(null)
        router.onAttachView()
        val observer = router.menuUpdater.subscribeOnTestObserver()

        router.replace(Foo)

        observer.assertValue(Menu.Input.SelectMenuItem(FooBar))
    }

    @Test
    fun `overlay dialog configuration - shows overlay dialog`() {
        router.onAttach(null)
        router.onAttachView()

        router.pushOverlay(Dialog)

        verify(dialogLauncher).show(eq(dialogToTestOverlay), any())
    }

    @Test
    fun `blocker configuration - attaches blocker and menu nodes`() {
        router.onAttach(null)
        router.onAttachView()

        router.replace(Blocker)

        assertThat(rootNode.getChildren()).containsExactlyInAnyOrder(menuNode, blockerNode)
    }

    private inline fun <reified B : Builder<*>> createBuilder(noinline buildCall: B.() -> Node<*>) =
        mock<B> {
            on(buildCall) doReturn mock(name = "Node mock for ${B::class.java.simpleName}")
        }
}
