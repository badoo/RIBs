package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.core.builder.BuildParams
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
        router = DialogExampleRouter(
            buildParams = BuildParams.Empty(),
            dialogLauncher = mock(),
            simpleDialog = mock(),
            lazyDialog = mock(),
            ribDialog = mock()
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
