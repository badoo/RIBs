package com.badoo.ribs.samples.back_stack.rib.child_e

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.back_stack.R

interface ChildEView : RibView {

    interface Factory : ViewFactory<Nothing?, ChildEView>

}


class ChildEViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildEView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_e
    ) : ChildEView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ChildEView = {
            ChildEViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
