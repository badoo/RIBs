package com.badoo.ribs.core.routing.state.exception

import com.badoo.ribs.core.routing.Routing

class KeyNotFoundInPoolException internal constructor(
    key: Routing<*>,
    pool: Any
) : IllegalStateException(
    "Key $key was not found in pool: $pool"
) {
}
