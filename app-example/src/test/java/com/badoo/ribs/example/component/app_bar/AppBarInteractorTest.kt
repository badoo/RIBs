package com.badoo.ribs.example.component.app_bar

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppBarInteractorTest {

    private val buildParams: BuildParams<Nothing?> = mock()
    private val backStack: BackStackFeature<Configuration> = mock()
    private val output: Consumer<AppBar.Output> = mock()
    private lateinit var interactor: AppBarInteractor

    @Before
    fun setup() {
        interactor = AppBarInteractor(
            buildParams = buildParams,
            backStack = backStack,
            output = output
        )
    }

    @After
    fun tearDown() {
    }

    /**
     * TODO: Add real tests.
     */
    @Test
    fun `an example test with some conditions should pass`() {
        throw RuntimeException("Add real tests.")
    }
}
