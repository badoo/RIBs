package com.badoo.ribs.example.rib.switcher

import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.STARTED
import com.badoo.common.ribs.InteractorTestHelper
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.switcher.SwitcherView.Event
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class SwitcherInteractorTest {

    private val router: Router<Configuration, Permanent, Content, Overlay, SwitcherView> = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()
    private val viewEventRelay = PublishRelay.create<Event>()

    private lateinit var switcherInteractor: SwitcherInteractor
    private lateinit var interactorTestHelper: InteractorTestHelper<SwitcherView>

    @Before
    fun setup() {
        switcherInteractor = SwitcherInteractor(
            buildParams = BuildParams.Empty(),
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
