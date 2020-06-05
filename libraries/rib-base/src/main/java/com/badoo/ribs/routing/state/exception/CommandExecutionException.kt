package com.badoo.ribs.routing.state.exception

class CommandExecutionException(message: String?, cause: Throwable?) :
    IllegalStateException(message, cause) {
}
