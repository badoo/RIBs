package com.badoo.ribs.core.routing.state.feature

import com.badoo.ribs.core.routing.history.Routing

class KeyNotFoundInPoolException internal constructor(
    key: Routing<*>,
    pool: Any
) : IllegalStateException(
    "Key $key was not found in pool: $pool"
) {
}
