package com.badoo.ribs.sandbox.rib.switcher

import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.STARTED
import com.badoo.common.ribs.InteractorTestHelper
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.configuration.feature.operation.pushOverlay
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class SwitcherInteractorTest {

    private val backStack: BackStackFeature<Configuration> = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val viewEventRelay = PublishRelay.create<Event>()
    private lateinit var interactor: SwitcherInteractor
    private lateinit var interactorTestHelper: InteractorTestHelper<SwitcherView>

    @Before
    fun setup() {
        interactor = SwitcherInteractor(
            buildParams = BuildParams.Empty(),
            backStack = backStack,
            dialogToTestOverlay = dialogToTestOverlay
        )

        interactorTestHelper = InteractorTestHelper.create(interactor, viewEventRelay)
    }

    @Test
    fun `router open overlay dialog when show overlay dialog clicked`(){
        interactorTestHelper.moveToStateAndCheck(STARTED) {
            viewEventRelay.accept(Event.ShowOverlayDialogClicked)

            verify(backStack).pushOverlay(Overlay.Dialog)
        }
    }

    @Test
    fun `router open blocker when show blocker clicked`(){
        interactorTestHelper.moveToStateAndCheck(STARTED) {
            viewEventRelay.accept(Event.ShowBlockerClicked)

            verify(backStack).push(Content.Blocker)
        }
    }

    @Test
    fun `skip view event when view not resumed`(){
        interactorTestHelper.moveToStateAndCheck(CREATED) {
            viewEventRelay.accept(Event.ShowBlockerClicked)

            verify(backStack, never()).push(Content.Blocker)
        }
    }
}
