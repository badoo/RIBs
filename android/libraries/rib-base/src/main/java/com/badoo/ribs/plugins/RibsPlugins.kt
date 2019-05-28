package com.badoo.ribs.plugins

import android.util.Log
import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStream

object RibsPlugins {

    private const val TAG = "RIBs"

    var noRequestCodeListenersErrorHandler
        : (requestCode: Int,
           internalRequestCode: Int,
           internalGroup: Int,
           event: RequestCodeBasedEventStream.RequestCodeBasedEvent) -> Int =
        { requestCode, internalRequestCode, internalGroup, event ->
            Log.e(TAG, "There's no one listening for request code event! " +
                "requestCode: $requestCode, " +
                "resolved group: $internalGroup, " +
                "resolved code: $internalRequestCode, " +
                "event: $event")
        }

}
