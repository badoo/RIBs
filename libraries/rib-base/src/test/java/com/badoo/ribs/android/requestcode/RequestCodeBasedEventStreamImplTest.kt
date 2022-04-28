package com.badoo.ribs.android.requestcode

import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.util.RIBs
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class RequestCodeBasedEventStreamImplTest {

    private val cancellables = CompositeCancellable()

    @Before
    fun setUp() {
        RIBs.clearErrorHandler()
        RIBs.errorHandler = mock()
    }

    @After
    fun tearDown() {
        RIBs.clearErrorHandler()
        cancellables.cancel()
    }

    @Test
    fun `publish - no clients listening for the response - calls unhandled request code handler`() {
        val stream = TestRequestCodeBasedEventStream()
        val internalRequestCode = stream.convertToInternalRequestCode(1)
        val event = TestEvent(internalRequestCode)

        stream.testPublish(1, event)

        verify(RIBs.errorHandler).handleNoRequestCodeListenersError(eq(1), eq(internalRequestCode), any(), eq(event))
    }

    @Test
    fun `publish - there are clients listening for the response - does not call unhandled request code handler`() {
        val stream = TestRequestCodeBasedEventStream()
        val identifiable = TestRequestCodeClient(requestCodeClientId = "test")
        val externalRequestCode = stream.convertToExternalRequestCode(identifiable, 1)
        val event = TestEvent(stream.convertToInternalRequestCode(externalRequestCode))
        cancellables += stream.events(identifiable).observe {}

        stream.testPublish(externalRequestCode, event)

        verify(RIBs.errorHandler, never()).handleNoRequestCodeListenersError(any(), any(), any(), any())
    }

    class TestRequestCodeClient(override val requestCodeClientId: String) : RequestCodeClient

    class TestEvent(override val requestCode: Int) : RequestCodeBasedEventStream.RequestCodeBasedEvent

    class TestRequestCodeBasedEventStream : RequestCodeBasedEventStreamImpl<TestEvent>(requestCodeRegistry = RequestCodeRegistry(null)) {

        fun testPublish(externalRequestCode: Int, event: TestEvent) {
            publish(externalRequestCode, event)
        }

        fun convertToInternalRequestCode(externalRequestCode: Int): Int =
            externalRequestCode.toInternalRequestCode()

        fun convertToExternalRequestCode(client: RequestCodeClient, internalRequestCode: Int): Int =
            client.forgeExternalRequestCode(internalRequestCode)

    }
}
