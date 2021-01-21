package com.samples.back_stack.content.content_d

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.samples.back_stack.R

interface ContentDView : RibView {

    interface Factory : ViewFactory<Nothing?, ContentDView>

}


class ContentDViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContentDView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_content_d
    ) : ContentDView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ContentDView = {
            ContentDViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
