package com.badoo.ribs.example.rib.main_foo_bar.viewplugin

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.badoo.ribs.core.view.ViewPlugin

class ParentLongClickListener : ViewPlugin {

    private val listener = View.OnLongClickListener {
        Log.d("ParentLongClickListener", "onLongClick")
        false
    }

    override fun onAttachtoView(parentViewGroup: ViewGroup) {
        parentViewGroup.setOnLongClickListener(listener)
    }

    override fun onDetachFromView(parentViewGroup: ViewGroup) {
        parentViewGroup.setOnLongClickListener(null)
    }
}
