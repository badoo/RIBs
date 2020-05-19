package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.DialogRoutingAction
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherConnections
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.sandbox.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Answers

/**
 * Unit tests that check only router
 * It may be helpful in case of complex routing logic
 */
class SwitcherRouterTest {

    private val connections: SwitcherConnections = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS)
    private val dialogLauncher: DialogLauncher = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val router =
        SwitcherRouter(
            BuildParams.Empty(),
            transitionHandler = null,
            connections = connections,
            dialogLauncher = dialogLauncher,
            dialogToTestOverlay = dialogToTestOverlay
        )

    @Before
    fun setUp() {
        whenever(connections.menu.build(any())).thenReturn(mock())
        whenever(connections.helloWorld.build(any())).thenReturn(mock())
        whenever(connections.fooBar.build(any())).thenReturn(mock())
        whenever(connections.dialogExample.build(any())).thenReturn(mock())
        whenever(connections.blocker.build(any())).thenReturn(mock())
    }

    @Test
    fun `Permanent_Menu configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Permanent.Menu).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(connections.menu).build(any())
    }

    @Test
    fun `Content_Hello configuration resolves to correct Node`() {
        val routingAction = router.resolveConfiguration(Content.Hello).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(connections.helloWorld).build(any())
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
        routingAction.buildNodes(listOf(root(null)))

        verify(connections.fooBar).build(any())
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
        routingAction.buildNodes(listOf(root(null)))

        verify(connections.dialogExample).build(any())
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
        routingAction.buildNodes(listOf(root(null)))

        verify(connections.blocker).build(any())
    }

    @Test
    fun `Overlay_Dialog configuration resolves to DialogRoutingAction`() {
        val routingAction = router.resolveConfiguration(Overlay.Dialog).apply { execute() }

        assertThat(routingAction).isInstanceOf(DialogRoutingAction::class.java)
    }
}
