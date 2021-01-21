package com.samples.back_stack.content.content_c

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.samples.back_stack.R

interface ContentCView : RibView {

    interface Factory : ViewFactory<Nothing?, ContentCView>

}


class ContentCViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContentCView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_content_c
    ) : ContentCView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ContentCView = {
            ContentCViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
