package com.badoo.ribs.core.routing.state.feature

class CommandExecutionException(message: String?, cause: Throwable?) :
    IllegalStateException(message, cause) {
}
