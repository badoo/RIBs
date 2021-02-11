package com.badoo.ribs.android.requestcode

import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStream.RequestCodeBasedEvent
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.util.RIBs

abstract class RequestCodeBasedEventStreamImpl<T : RequestCodeBasedEvent>(
    private val requestCodeRegistry: RequestCodeRegistry
) : RequestCodeBasedEventStream<T> {
    private val events = HashMap<Int, Relay<T>>()

    override fun events(client: RequestCodeClient): Source<T> {
        val id = requestCodeRegistry.generateGroupId(client.requestCodeClientId)
        ensureSubject(id)

        return events.getValue(id)
    }

    private fun ensureSubject(id: Int, onSubjectDidNotExist: (() -> Unit)? = null) {
        var subjectJustCreated = false

        if (!events.containsKey(id)) {
            events[id] = Relay()
            subjectJustCreated = true
        }

        if (subjectJustCreated) {
            onSubjectDidNotExist?.invoke()
        }
    }

    protected fun publish(externalRequestCode: Int, event: T) {
        val id = requestCodeRegistry.resolveGroupId(externalRequestCode)
        val internalRequestCode = externalRequestCode.toInternalRequestCode()

        ensureSubject(id) {
            RIBs.errorHandler.handleNoRequestCodeListenersError(externalRequestCode, internalRequestCode, id, event)
        }

        events.getValue(id).emit(event)
    }

    protected fun Int.toInternalRequestCode() =
        requestCodeRegistry.resolveRequestCode(this)

    protected fun RequestCodeClient.forgeExternalRequestCode(internalRequestCode: Int) =
        requestCodeRegistry.generateRequestCode(this.requestCodeClientId, internalRequestCode)
}
