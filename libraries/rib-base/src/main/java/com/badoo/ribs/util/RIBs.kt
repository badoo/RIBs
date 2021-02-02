package com.badoo.ribs.util

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStream

object RIBs {

    private const val TAG = "RIBs"

    private var _errorHandler: ErrorHandler? = null

    var errorHandler: ErrorHandler
        get() {
            return _errorHandler ?: DefaultErrorHandler().also {
                _errorHandler = it
            }
        }
        set(value) {
            if (_errorHandler == null) {
                _errorHandler = value
            } else {
                if (_errorHandler is DefaultErrorHandler) {
                    throw IllegalStateException("Attempting to set a errorHandler after using RIB code.")
                } else {
                    throw IllegalStateException("Attempting to set a errorHandler after one has previously been set.")
                }
            }
        }

    @VisibleForTesting
    fun clearErrorHandler() {
        _errorHandler = null
    }

    interface ErrorHandler {

        fun handleFatalError(errorMessage: String, throwable: Throwable? = null): Nothing

        /**
         * Called when there is a non-fatal error in the RIB framework. Consumers should route this data
         * to a place where it can be monitored (crash reporting, monitoring, etc.).
         *
         *
         * If no errorHandler is set, the default implementation of this will crash the app when
         * there is a non-fatal error.
         *
         * @param errorMessage an error message that describes the error.
         * @param throwable an optional throwable.
         */
        fun handleNonFatalError(errorMessage: String, throwable: Throwable? = null)

        /**
         * Called when there is a non-fatal warning in the RIB framework. Consumers should route this
         * data to a place where it can be monitored (crash reporting, monitoring, etc.).
         *
         *
         * NOTE: This API is used in a slightly different way than the [ ][ErrorHandler.handleNonFatalError] error method. Non-fatal errors should
         * never happen, warnings however can happen in certain conditions.
         *
         * @param warningMessage an error message that describes the error.
         * @param throwable an optional throwable.
         */
        fun handleNonFatalWarning(warningMessage: String, throwable: Throwable? = null)

        /**
         * Called when there is a message that should be logged for debugging. Consumers should route
         * this data to a debug logging location.
         *
         *
         * If no errorHandler is set, the default implementation of this will drop the messages.
         *
         * @param format Message format - See [String.format]
         * @param args Arguments to use for printing the message.
         */
        fun handleDebugMessage(format: String, vararg args: Any)


        /**
         * Called when we received a result for request code but there is no listeners for it
         *
         * If no errorHandler is set, the default implementation of this will print a warning
         * to logcat.
         */
        fun handleNoRequestCodeListenersError(
            requestCode: Int,
            internalRequestCode: Int,
            internalGroup: Int,
            event: RequestCodeBasedEventStream.RequestCodeBasedEvent
        )
    }

    private class DefaultErrorHandler : ErrorHandler {

        override fun handleNoRequestCodeListenersError(
            requestCode: Int,
            internalRequestCode: Int,
            internalGroup: Int,
            event: RequestCodeBasedEventStream.RequestCodeBasedEvent
        ) {
            Log.e(TAG, "There's no one listening for request code event! " +
                "requestCode: $requestCode, " +
                "resolved group: $internalGroup, " +
                "resolved code: $internalRequestCode, " +
                "event: $event")
        }

        @Suppress("TooGenericExceptionThrown")
        override fun handleFatalError(errorMessage: String, throwable: Throwable?): Nothing {
            throw RuntimeException(errorMessage, throwable)
        }

        @Suppress("TooGenericExceptionThrown")
        override fun handleNonFatalError(errorMessage: String, throwable: Throwable?) {
            throw RuntimeException(errorMessage, throwable)
        }

        override fun handleNonFatalWarning(warningMessage: String, throwable: Throwable?) {}

        override fun handleDebugMessage(format: String, vararg args: Any) {
            Log.d(TAG, format.format(args))
        }
    }
}
