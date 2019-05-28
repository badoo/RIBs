package com.badoo.ribs.android.requestcode

import com.badoo.ribs.core.Identifiable
import com.badoo.ribs.plugins.RibsPlugins
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequestCodeBasedEventStreamImplTest {

    private var originalHandler: ((requestCode: Int,
                                   internalRequestCode: Int,
                                   internalGroup: Int,
                                   event: RequestCodeBasedEventStream.RequestCodeBasedEvent) -> Int)? = null

    private val disposables = CompositeDisposable()

    @Before
    fun setUp() {
        originalHandler = RibsPlugins.noRequestCodeListenersErrorHandler
        RibsPlugins.noRequestCodeListenersErrorHandler = mock()
    }

    @After
    fun tearDown() {
        originalHandler?.let {
            RibsPlugins.noRequestCodeListenersErrorHandler = it
        }
        disposables.dispose()
    }

    @Test
    fun `publish - no clients listening for the response - calls unhandled request code handler`() {
        val stream = TestRequestCodeBasedEventStream()
        val internalRequestCode = stream.convertToInternalRequestCode(1)
        val event = TestEvent(internalRequestCode)

        stream.testPublish(1, event)

        verify(RibsPlugins.noRequestCodeListenersErrorHandler).invoke(eq(1), eq(internalRequestCode), any(), eq(event))
    }

    @Test
    fun `publish - there are clients listening for the response - does not call unhandled request code handler`() {
        val stream = TestRequestCodeBasedEventStream()
        val identifiable = TestIdentifiable(id = "test")
        val externalRequestCode = stream.convertToExternalRequestCode(identifiable, 1)
        val event = TestEvent(stream.convertToInternalRequestCode(externalRequestCode))
        disposables.add(stream.events(identifiable).subscribe())

        stream.testPublish(externalRequestCode, event)

        verify(RibsPlugins.noRequestCodeListenersErrorHandler, never()).invoke(any(), any(), any(), any())
    }

    class TestIdentifiable(override val id: String) : Identifiable

    class TestEvent(override val requestCode: Int) : RequestCodeBasedEventStream.RequestCodeBasedEvent

    class TestRequestCodeBasedEventStream : RequestCodeBasedEventStreamImpl<TestEvent>(requestCodeRegistry = RequestCodeRegistry(null)) {

        fun testPublish(externalRequestCode: Int, event: TestEvent) {
            publish(externalRequestCode, event)
        }

        fun convertToInternalRequestCode(externalRequestCode: Int): Int =
            externalRequestCode.toInternalRequestCode()

        fun convertToExternalRequestCode(identifiable: Identifiable, internalRequestCode: Int): Int =
            identifiable.forgeExternalRequestCode(internalRequestCode)

    }
}
