package com.badoo.ribs.sandbox.rib.switcher

import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.STARTED
import com.badoo.common.ribs.rx2.createInteractorTestHelper
import com.badoo.ribs.routing.router.Router.TransitionState.SETTLED
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.test.InteractorTestHelper
import com.badoo.ribs.test.emptyBuildParams
import com.jakewharton.rxrelay2.PublishRelay
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SwitcherInteractorTest {

    private val backStack: BackStack<Configuration> = mock()
    private val dialogToTestOverlay: DialogToTestOverlay = mock()

    private val viewEventRelay = PublishRelay.create<Event>()
    private lateinit var interactor: SwitcherInteractor
    private lateinit var interactorTestHelper: InteractorTestHelper<SwitcherView>

    @Before
    fun setup() {
        interactor = SwitcherInteractor(
            buildParams = emptyBuildParams(),
            backStack = backStack,
            dialogToTestOverlay = dialogToTestOverlay,
            transitions = Observable.just(SETTLED),
            transitionSettled = { true }
        )

        interactorTestHelper = createInteractorTestHelper(interactor, viewEventRelay)
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
