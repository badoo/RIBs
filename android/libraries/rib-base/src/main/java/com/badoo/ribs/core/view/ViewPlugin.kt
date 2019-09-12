package com.badoo.ribs.core.view

import android.view.ViewGroup

interface ViewPlugin {

    fun onAttachtoView(parentViewGroup: ViewGroup)

    fun onDetachFromView(parentViewGroup: ViewGroup)
}
