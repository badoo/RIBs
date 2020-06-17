package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class ComposeParentRouterTest {

    private var router: ComposeParentRouter? = null

    @Before
    fun setup() {
        router = ComposeParentRouter(
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
