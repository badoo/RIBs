package com.badoo.ribs.routing.state.exception

import com.badoo.ribs.routing.Routing

class KeyNotFoundInPoolException internal constructor(
    key: Routing<*>,
    pool: Any
) : IllegalStateException(
    "Key $key was not found in pool: $pool"
) {
}
