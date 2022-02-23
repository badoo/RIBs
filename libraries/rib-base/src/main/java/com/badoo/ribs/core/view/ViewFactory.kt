package com.badoo.ribs.core.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewTreeLifecycleOwner

fun interface ViewFactory<View> : (ViewFactory.Context) -> View {
    class Context(
        internal val parent: RibView,
        val lifecycle: Lifecycle,
    ) {
        val context: android.content.Context get() = parent.context

        fun <T : View> inflate(@LayoutRes layoutResourceId: Int): T {
            val view = parent.androidView.inflate<T>(layoutResourceId)
            ViewTreeLifecycleOwner.set(view) { lifecycle }
            return view
        }

        private fun <T> ViewGroup.inflate(@LayoutRes layoutResourceId: Int): T =
            LayoutInflater
                .from(context)
                .inflate(
                    layoutResourceId,
                    this,
                    false,
                ) as T
    }
}
