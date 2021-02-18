package com.badoo.ribs.samples.back_stack.rib.child_b

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.back_stack.R

interface ChildBView : RibView {

    interface Factory : ViewFactory<Nothing?, ChildBView>

}


class ChildBViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildBView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_b
    ) : ChildBView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ChildBView = {
            ChildBViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
