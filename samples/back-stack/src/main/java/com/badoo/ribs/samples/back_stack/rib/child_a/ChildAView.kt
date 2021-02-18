package com.badoo.ribs.samples.back_stack.rib.child_a

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.back_stack.R

interface ChildAView : RibView {

    interface Factory : ViewFactory<Nothing?, ChildAView>

}


class ChildAViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildAView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_a
    ) : ChildAView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ChildAView = {
            ChildAViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
