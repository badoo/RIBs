package com.badoo.ribs.core.routing.configuration.feature

import com.badoo.ribs.core.routing.configuration.ConfigurationKey

class KeyNotFoundInPoolException internal constructor(
    key: ConfigurationKey<*>,
    pool: Any
) : IllegalStateException(
    "Key $key was not found in pool: $pool"
) {
}
