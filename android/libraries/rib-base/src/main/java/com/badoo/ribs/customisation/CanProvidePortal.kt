package com.badoo.ribs.customisation

import com.badoo.ribs.core.routing.portal.Portal


interface CanProvidePortal {
    fun portal(): Portal.Sink
}
