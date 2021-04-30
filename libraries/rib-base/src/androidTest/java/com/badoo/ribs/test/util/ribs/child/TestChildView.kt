package com.badoo.ribs.test.util.ribs.child

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView

interface TestChildView : RibView

class TestChildViewImpl(
    context: Context,
    addEditText: Boolean,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AndroidRibView(),
    TestChildView {

    override val androidView: ViewGroup by lazy {
        FrameLayout(context, attrs, defStyle).also {
            if (addEditText) {
                it.addView(createEditTextView(context))
            }
        }
    }

    private fun createEditTextView(context: Context): EditText {
        val editText = EditText(context)
        editText.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        return editText
    }
}
