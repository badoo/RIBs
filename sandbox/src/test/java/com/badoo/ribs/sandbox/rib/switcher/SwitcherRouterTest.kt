package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.routing.resolution.DialogResolution
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherChildBuilders
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.test.emptyBuildParams
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
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

    private val builders: SwitcherChildBuilders = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS)
    private val dialogLauncher: DialogLauncher = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val router =
        SwitcherRouter(
            buildParams = emptyBuildParams(),
            routingSource = Empty(),
            transitionHandler = null,
            builders = builders,
            dialogLauncher = dialogLauncher,
            dialogToTestOverlay = dialogToTestOverlay
        )

    @Before
    fun setUp() {
        whenever(builders.menu.build(any())).doReturn(mock())
        whenever(builders.helloWorld.build(any())).doReturn(mock())
        whenever(builders.fooBar.build(any())).doReturn(mock())
        whenever(builders.dialogExample.build(any())).doReturn(mock())
        whenever(builders.blocker.build(any())).doReturn(mock())
    }

    @Test
    fun `Permanent_Menu configuration resolves to correct Node`() {
        val routingAction = router.resolve(Routing(configuration = Permanent.Menu)).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(builders.menu).build(any())
    }

    @Test
    fun `Content_Hello configuration resolves to correct Node`() {
        val routingAction = router.resolve(Routing(configuration = Content.Hello)).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(builders.helloWorld).build(any())
    }

    @Test
    fun `Content_Foo configuration resolves to correct Node`() {
        val routingAction = router.resolve(Routing(configuration = Content.Foo)).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(builders.fooBar).build(any())
    }

    @Test
    fun `Content_DialogsExample configuration resolves to correct Node`() {
        val routingAction = router.resolve(Routing(configuration = Content.DialogsExample)).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(builders.dialogExample).build(any())
    }

    @Test
    fun `Content_Blocker configuration resolves to correct Node`() {
        val routingAction = router.resolve(Routing(configuration = Content.Blocker)).apply { execute() }
        routingAction.buildNodes(listOf(root(null)))

        verify(builders.blocker).build(any())
    }

    @Test
    fun `Overlay_Dialog configuration resolves to DialogRoutingAction`() {
        val routingAction = router.resolve(Routing(configuration = Overlay.Dialog)).apply { execute() }

        assertThat(routingAction).isInstanceOf(DialogResolution::class.java)
    }
}
