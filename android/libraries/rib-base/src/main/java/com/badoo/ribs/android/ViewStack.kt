package com.badoo.ribs.android

import android.view.View
import android.view.ViewGroup

class ViewStackImpl(
    private val container: ViewGroup
) {
//    private val cache = mutableMapOf<Int, View>()

    private val views = mutableListOf<View>()

//    private fun findView(id: Int) =
//        cache[id] ?: container.findViewById<View>(id).also { cache[id] = it }


    fun push(view: View) {
        views.lastOrNull()?.visibility = View.GONE
        view.let {
            it.visibility = View.VISIBLE
            views.add(it)
            container.addView(it)
        }
    }

    fun pop() {
        views.dropLast(1)
        views.lastOrNull()?.visibility = View.VISIBLE
    }
}
