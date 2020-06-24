package com.badoo.ribs.example.welcome

import androidx.lifecycle.Lifecycle
import com.badoo.common.ribs.InteractorTestHelper
import com.badoo.common.ribs.InteractorTestHelper.Companion.mockIO
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.welcome.Welcome.Output
import com.badoo.ribs.example.welcome.WelcomeView.Event
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
            buildParams = BuildParams.Empty(),
            authDataSource = authDataSource
        )
        interactor.mockIO(outputRelay = output)
        interactorTestHelper = InteractorTestHelper.create(interactor, viewEventRelay)
        outputObserver = TestObserver.create()
        output.subscribe(outputObserver)
    }

    @Test
    fun `an example test with some conditions should pass`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            viewEventRelay.accept(Event.SkipClicked)

            verify(authDataSource).loginAnonymous()
        }
    }

    @Test
    fun `an example test with some conditions output`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            viewEventRelay.accept(Event.LoginClicked)

            outputObserver.assertValue(Output.LoginClicked)
        }
    }

    @Test
    fun `an example test with some conditions output regiser`() {
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            viewEventRelay.accept(Event.RegisterClicked)

            outputObserver.assertValue(Output.RegisterClicked)
        }
    }
}
