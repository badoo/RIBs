package com.badoo.ribs.samples.back_stack.content.content_b

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.back_stack.R

interface ContentBView : RibView {

    interface Factory : ViewFactory<Nothing?, ContentBView>

}


class ContentBViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContentBView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_content_b
    ) : ContentBView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ContentBView = {
            ContentBViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
