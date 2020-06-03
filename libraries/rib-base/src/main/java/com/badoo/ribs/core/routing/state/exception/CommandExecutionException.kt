package com.badoo.ribs.core.routing.state.exception

class CommandExecutionException(message: String?, cause: Throwable?) :
    IllegalStateException(message, cause) {
}
