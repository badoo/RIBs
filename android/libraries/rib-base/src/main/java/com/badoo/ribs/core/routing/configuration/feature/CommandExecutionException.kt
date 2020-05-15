package com.badoo.ribs.core.routing.configuration.feature

class CommandExecutionException(message: String?, cause: Throwable?) :
    IllegalStateException(message, cause) {
}
