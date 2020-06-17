package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.sandbox.rib.compose_parent.feature.ComposeParentFeature
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class ComposeParentInteractorTest {

    private val feature: ComposeParentFeature = mock()
    private val backStack: BackStackFeature<Configuration> = mock()
    private lateinit var interactor: ComposeParentInteractor

    @Before
    fun setup() {
        interactor = ComposeParentInteractor(
            buildParams = BuildParams.Empty(),
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
