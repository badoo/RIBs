package com.badoo.ribs.test.util.ribs.child

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.badoo.ribs.core.view.RibView

interface TestChildView : RibView

class TestChildViewImpl(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), TestChildView {
    override val androidView = this
}
