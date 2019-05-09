package com.badoo.ribs.core.view

import android.view.ViewGroup
import com.badoo.ribs.core.Rib

interface RibView {

    val androidView: ViewGroup

    fun getParentViewForChild(child: Rib): ViewGroup? =
        androidView
}
