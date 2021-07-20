package com.badoo.ribs.example.welcome

import androidx.lifecycle.Lifecycle
import com.badoo.common.ribs.rx2.subscribedView
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.welcome.Welcome.Output
import com.badoo.ribs.example.welcome.WelcomeView.Event
import com.badoo.ribs.test.InteractorTestHelper
import com.badoo.ribs.test.emptyBuildParams
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WelcomeInteractorTest {

    private lateinit var interactor: WelcomeInteractor
    private val authDataSource: AuthDataSource = mock()
    private lateinit var interactorTestHelper: InteractorTestHelper<WelcomeView>
    private val viewEventRelay = PublishRelay.create<Event>()
    private lateinit var outputObserver: TestObserver<Output>
    private val output = PublishRelay.create<Output>()


    @BeforeEach
    fun setup() {
        interactor = WelcomeInteractor(
            buildParams = emptyBuildParams(),
            authDataSource = authDataSource
        )
        val view = viewEventRelay.subscribedView<WelcomeView, Event>()
        interactorTestHelper = InteractorTestHelper(interactor)
        interactorTestHelper.nodeCreator = {
            object : Node<WelcomeView>(
                buildParams = emptyBuildParams(),
                viewFactory = { view },
                plugins = listOf(interactor)
            ), Welcome {
                override val input: Relay<Welcome.Input> = PublishRelay.create()
                override val output: Relay<Output> = this@WelcomeInteractorTest.output
            }
        }
        outputObserver = TestObserver.create()
        output.subscribe(outputObserver)
    }

    @Test
    fun `when skip auth clicked then loginAnonymous called`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            viewEventRelay.accept(Event.SkipClicked)

            verify(authDataSource).loginAnonymous()
        }
    }

    @Test
    fun `when LoginClicked then output LoginClicked`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            viewEventRelay.accept(Event.LoginClicked)

            outputObserver.assertValue(Output.LoginClicked)
        }
    }

    @Test
    fun `when RegisterClicked then output RegisterClicked`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            viewEventRelay.accept(Event.RegisterClicked)

            outputObserver.assertValue(Output.RegisterClicked)
        }
    }
}
