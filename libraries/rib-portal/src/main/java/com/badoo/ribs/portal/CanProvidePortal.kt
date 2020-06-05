package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi

interface CanProvidePortal {

    @ExperimentalApi
    fun portal(): Portal.OtherSide
}
