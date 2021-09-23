package com.badoo.ribs.compose

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.MutableState
import androidx.compose.ui.viewinterop.AndroidView

class ComposeChildHost(
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
        container.addView(view)
    }

    fun removeView(view: View) {
        container.removeView(view)
    }
}

