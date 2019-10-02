package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestInteractor
import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InteractorTest {

    private lateinit var interactor: Interactor<TestRouter.Configuration, TestRouter.Configuration, Nothing, TestView>

    @Before
    fun setUp() {
        interactor = TestInteractor(
            buildContext = mock(),
            router = mock(),
            disposables = null
        )
    }
}
