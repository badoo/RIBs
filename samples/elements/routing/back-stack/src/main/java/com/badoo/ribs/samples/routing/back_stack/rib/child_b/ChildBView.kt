package com.badoo.ribs.samples.routing.back_stack.rib.child_b

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.back_stack.R

interface ChildBView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, ChildBView>

}


class ChildBViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildBView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_b
    ) : ChildBView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ChildBView> = ViewFactory {
            ChildBViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
