package com.badoo.ribs.sandbox.rib.lorem_ipsum

import com.badoo.ribs.test.emptyBuildParams
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoremIpsumInteractorTest {

    private lateinit var interactor: LoremIpsumInteractor

    @Before
    fun setup() {
        interactor = LoremIpsumInteractor(
            buildParams = emptyBuildParams()
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
