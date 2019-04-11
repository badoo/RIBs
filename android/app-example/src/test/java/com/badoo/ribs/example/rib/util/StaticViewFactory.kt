package com.badoo.ribs.example.rib.util

import android.view.ViewGroup
import com.badoo.ribs.core.view.ViewFactory

class StaticViewFactory<T : Any>(private val view: T) : ViewFactory<T> {
    override fun invoke(viewGroup: ViewGroup): T = view
}
