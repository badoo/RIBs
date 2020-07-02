package com.badoo.ribs.example.photo_details

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter.Configuration
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class PhotoDetailsInteractorTest {

    private val feature: PhotoDetailsFeature = mock()
    private val backStack: BackStackFeature<Configuration> = mock()
    private lateinit var interactor: PhotoDetailsInteractor

    @Before
    fun setup() {
        interactor = PhotoDetailsInteractor(
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
