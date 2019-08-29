package com.badoo.ribs.core.routing.portal

import android.os.Parcelable

interface Portal {

    fun push(configurationChain: List<Parcelable>)
}
