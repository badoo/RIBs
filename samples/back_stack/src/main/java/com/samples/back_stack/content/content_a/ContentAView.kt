package com.samples.back_stack.content.content_a

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.samples.back_stack.R

interface ContentAView : RibView {

    interface Factory : ViewFactory<Nothing?, ContentAView>

}


class ContentAViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContentAView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_content_a
    ) : ContentAView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ContentAView = {
            ContentAViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
