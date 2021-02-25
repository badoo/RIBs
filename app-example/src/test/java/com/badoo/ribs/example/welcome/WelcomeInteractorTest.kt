package com.badoo.ribs.example.welcome

import androidx.lifecycle.Lifecycle
import com.badoo.common.ribs.rx2.createInteractorTestHelper
import com.badoo.common.ribs.rx2.mockIO
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.welcome.Welcome.Output
import com.badoo.ribs.example.welcome.WelcomeView.Event
import com.badoo.ribs.test.InteractorTestHelper
import com.badoo.ribs.test.emptyBuildParams
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class WelcomeInteractorTest {

    private lateinit var interactor: WelcomeInteractor
    private val authDataSource: AuthDataSource = mock()
    private lateinit var interactorTestHelper: InteractorTestHelper<WelcomeView>
    private val viewEventRelay = PublishRelay.create<Event>()
    private lateinit var outputObserver: TestObserver<Output>
    private val output = PublishRelay.create<Output>()


    @Before
    fun setup() {
        interactor = WelcomeInteractor(
            buildParams = emptyBuildParams(),
            authDataSource = authDataSource
        )
        interactor.mockIO(outputRelay = output)
        interactorTestHelper = createInteractorTestHelper(interactor, viewEventRelay)
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
