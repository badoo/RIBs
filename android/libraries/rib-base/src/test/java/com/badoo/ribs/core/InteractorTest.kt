package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.Interactor.Companion.BUNDLE_KEY
import com.badoo.ribs.core.Interactor.Companion.KEY_TAG
import com.badoo.ribs.core.helper.TestInteractor
import com.badoo.ribs.core.helper.TestView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InteractorTest {

    private lateinit var interactor: Interactor<TestView>

    @Before
    fun setUp() {
        interactor = TestInteractor(
            router = mock(),
            disposables = null
        )
    }

    @Test
    fun `Tag is generated automatically`() {
        interactor.onAttach(mock())
        assertNotNull(interactor.tag)
    }

    @Test
    fun `Tag is saved to bundle`() {
        val outState = Bundle()
        interactor.onSaveInstanceState(outState)
        val inner = outState.getBundle(BUNDLE_KEY)
        assertNotNull(inner)
        assertEquals(interactor.tag, inner.getString(KEY_TAG))
    }

    @Test
    fun `Tag is restored from bundle`() {
        val savedInstanceState = mock<Bundle>()
        val interactorBundle = mock<Bundle>()
        whenever(savedInstanceState.getBundle(BUNDLE_KEY)).thenReturn(interactorBundle)
        whenever(interactorBundle.getString(KEY_TAG)).thenReturn("abcdef")
        interactor = TestInteractor(
            savedInstanceState = savedInstanceState,
            router = mock(),
            disposables = null
        )
        interactor.onAttach(mock())
        assertEquals("abcdef", interactor.tag)
    }
}
