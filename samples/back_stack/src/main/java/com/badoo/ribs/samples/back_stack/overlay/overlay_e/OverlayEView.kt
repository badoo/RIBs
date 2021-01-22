package com.badoo.ribs.samples.back_stack.overlay.overlay_e

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.back_stack.R

interface OverlayEView : RibView {

    interface Factory : ViewFactory<Nothing?, OverlayEView>

}


class OverlayEViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    OverlayEView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_overlay_e
    ) : OverlayEView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> OverlayEView = {
            OverlayEViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
