package com.badoo.ribs.example.photo_details

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class PhotoDetailsRouterTest {

    private var router: PhotoDetailsRouter? = null

    @Before
    fun setup() {
        router = PhotoDetailsRouter(
            buildParams = BuildParams.Empty(),
            builders = mock(defaultAnswer = Answers.RETURNS_MOCKS),
            routingSource = Empty()
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
