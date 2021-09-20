package com.badoo.ribs.compose

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.MutableState
import androidx.compose.ui.viewinterop.AndroidView

class ComposeTarget(
    composeViewState: MutableState<ComposeView?>,
    val container: ViewGroup,
) {
    constructor(
        composeViewState: MutableState<ComposeView?>,
        context: Context,
    ) : this(
        composeViewState = composeViewState,
        container = FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
    )

    private val composeAndroidView: ComposeView = { AndroidView(factory = { container }) }

    init {
        composeViewState.value = composeAndroidView
    }

    fun addView(view: View) {
        Log.d("COMPCONTAINER", "Adding: ${view.hashCode()}")
        container.addView(view)
        listChild(container)
    }

    private fun listChild(container: ViewGroup) {
        val count = container.childCount
        Log.d("COMPCONTAINER", "ChildCount: $count")

        if (count > 0) {
            val children = mutableListOf<String>().apply {
                for (i in 0 until count) {
                    add(
                        container.getChildAt(i).hashCode().toString()
                    )
                }
            }

            Log.d("COMPCONTAINER", "ChidlList: $children")
        }
    }

    fun removeView(view: View) {
        Log.d("COMPCONTAINER", "Destroying: ${view.hashCode()}")
        container.removeView(view)
        listChild(container)
    }
}

