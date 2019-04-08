package com.badoo.ribs.test.util.ribs.root

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.badoo.ribs.core.view.RibView

interface TestRootView : RibView

class TestRootViewImpl(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), TestRootView {
    override val androidView = this
}
