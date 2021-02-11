package com.badoo.ribs.android.requestcode

import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStream.RequestCodeBasedEvent
import com.badoo.ribs.core.state.Source

interface RequestCodeBasedEventStream<T : RequestCodeBasedEvent> {

    fun events(client: RequestCodeClient): Source<T>

    interface RequestCodeBasedEvent {
        val requestCode: Int
    }
}
