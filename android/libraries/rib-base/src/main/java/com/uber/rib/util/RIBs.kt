/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uber.rib.util

/** Holds errorHandler and settings for RIBs  */
object RIBs {

    private var errorHandler: ErrorHandler? = null

    /**
     * Sets the errorHandler to use in the application. This can only be called once before any RIB
     * code is used. Calling it twice, or calling it after using RIB code will throw an [ ].
     *
     * @param errorHandler to set.
     */
    fun setErrorHandler(errorHandler: ErrorHandler) {
        setErrorHandler(errorHandler, allowMoreThanOnce = false)
    }

    internal fun setErrorHandler(errorHandler: ErrorHandler, allowMoreThanOnce: Boolean) {
        if (RIBs.errorHandler == null || allowMoreThanOnce) {
            RIBs.errorHandler = errorHandler
        } else {
            if (RIBs.errorHandler is DefaultErrorHandler) {
                throw IllegalStateException("Attempting to set a errorHandler after using RIB code.")
            } else {
                throw IllegalStateException(
                    "Attempting to set a errorHandler after one has previously been set."
                )
            }
        }
    }

    internal fun resetErrorHandler() {
        errorHandler = null
    }

    fun getErrorHandler(): ErrorHandler {
        if (errorHandler == null) {
            errorHandler = DefaultErrorHandler()
        }

        return errorHandler!!
    }

    /** Responsible for app-specific riblet errorHandler.  */
    interface ErrorHandler {

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
        fun handleNonFatalError(errorMessage: String, throwable: Throwable?)

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
        fun handleNonFatalWarning(warningMessage: String, throwable: Throwable?)

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
    }

    /** Default, internal implementation that is used when host app does not set a errorHandler.  */
    private class DefaultErrorHandler : ErrorHandler {

        @SuppressWarnings("TooGenericExceptionThrown")
        override fun handleNonFatalError(errorMessage: String, throwable: Throwable?) {
            throw RuntimeException(errorMessage, throwable)
        }

        override fun handleNonFatalWarning(warningMessage: String, throwable: Throwable?) {}

        override fun handleDebugMessage(format: String, vararg args: Any) {}
    }
}
