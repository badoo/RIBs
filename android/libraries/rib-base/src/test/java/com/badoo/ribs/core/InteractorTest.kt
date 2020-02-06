package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestInteractor
import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InteractorTest {

    private lateinit var interactor: Interactor<TestRouter.Configuration, TestRouter.Configuration, Nothing, TestView>
    private val disposables = mock<Disposable>()

    @Before
    fun setUp() {
        interactor = TestInteractor(
            buildParams = mock(),
            router = mock(),
            disposables = disposables
        )
    }

    @Test
    fun `onDetach() disposes disposables`() {
        interactor.onDetach()
        verify(disposables).dispose()
    }
}
