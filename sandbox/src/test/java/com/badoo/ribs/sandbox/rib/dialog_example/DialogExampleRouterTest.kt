package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class DialogExampleRouterTest {

    private var router: DialogExampleRouter? = null

    @Before
    fun setup() {
        router =
            DialogExampleRouter(
                buildParams = BuildParams.Empty(),
                routingSource = Empty(),
                dialogLauncher = mock(),
                dialogs = mock()
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
    }
}
