package com.badoo.ribs.customisation

import com.badoo.ribs.core.Portal


interface CanProvidePortal {
    fun portal(): Portal.Sink
}
