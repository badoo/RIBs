package com.badoo.ribs.samples.back_stack.overlay.overlay_f

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.back_stack.R

interface OverlayFView : RibView {

    interface Factory : ViewFactory<Nothing?, OverlayFView>

}


class OverlayFViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    OverlayFView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_overlay_f
    ) : OverlayFView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> OverlayFView = {
            OverlayFViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
