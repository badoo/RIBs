package com.badoo.ribs.sandbox.rib.switcher

import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.STARTED
import com.badoo.common.ribs.InteractorTestHelper
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.configuration.feature.operation.pushOverlay
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherConnections
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Answers

class SwitcherInteractorTest {

    private val connections: SwitcherConnections = mock(defaultAnswer = Answers.RETURNS_DEEP_STUBS)
    private val router: SwitcherRouter = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()
    private val viewEventRelay = PublishRelay.create<Event>()

    private lateinit var switcherInteractor: SwitcherInteractor
    private lateinit var interactorTestHelper: InteractorTestHelper<SwitcherView>

    @Before
    fun setup() {
        switcherInteractor = SwitcherInteractor(
            buildParams = BuildParams.Empty(),
            connections = connections,
            router = router,
            dialogToTestOverlay = dialogToTestOverlay
        )

        interactorTestHelper = InteractorTestHelper.create(switcherInteractor, viewEventRelay, router)
    }

    @Test
    fun `router open overlay dialog when show overlay dialog clicked`(){
        interactorTestHelper.moveToStateAndCheck(STARTED) {
            viewEventRelay.accept(Event.ShowOverlayDialogClicked)

            verify(router).pushOverlay(Overlay.Dialog)
        }
    }

    @Test
    fun `router open blocker when show blocker clicked`(){
        interactorTestHelper.moveToStateAndCheck(STARTED) {
            viewEventRelay.accept(Event.ShowBlockerClicked)

            verify(router).push(Content.Blocker)
        }
    }

    @Test
    fun `skip view event when view not resumed`(){
        interactorTestHelper.moveToStateAndCheck(CREATED) {
            viewEventRelay.accept(Event.ShowBlockerClicked)

            verify(router, never()).push(Content.Blocker)
        }
    }
}
