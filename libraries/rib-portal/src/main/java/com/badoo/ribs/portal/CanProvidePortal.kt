package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi

@ExperimentalApi
interface CanProvidePortal {

    val portal: Portal.OtherSide
}
