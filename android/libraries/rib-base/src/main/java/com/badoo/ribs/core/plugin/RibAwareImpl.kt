package com.badoo.ribs.core.plugin

import com.badoo.ribs.core.Rib

class RibAwareImpl<T : Rib>: RibAware<T> {
    override lateinit var rib: T
        private set

    override fun init(rib: T) {
        this.rib = rib
    }
}
