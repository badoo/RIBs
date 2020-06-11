package com.badoo.ribs.example.component.app_bar

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.example.component.app_bar.feature.AppBarFeature
import com.badoo.ribs.example.component.app_bar.routing.AppBarRouter.Configuration
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppBarInteractorTest {

    private val buildParams: BuildParams<Nothing?> = mock()
    private val feature: AppBarFeature = mock()
    private val backStack: BackStackFeature<Configuration> = mock()
    private lateinit var interactor: AppBarInteractor

    @Before
    fun setup() {
        interactor = AppBarInteractor(
            buildParams = buildParams,
            feature = feature,
            backStack = backStack
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
