package com.badoo.ribs.test.sample

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory

interface SampleView : RibView

val viewId = View.generateViewId()

class SampleViewImpl(
    context: ViewFactory.Context
) : AndroidRibView(), SampleView {

    val lifecycle: Lifecycle = context.lifecycle

    override val androidView: ViewGroup = FrameLayout(context.parent.context)

    init {
        val child = TextView(context.parent.context)
        child.id = viewId
        child.text = TextView::class.qualifiedName
        androidView.addView(child)
    }

}
