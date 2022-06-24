package com.badoo.ribs.samples.routing.back_stack.rib.child_c

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.back_stack.R

interface ChildCView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, ChildCView>

}


class ChildCViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildCView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_c
    ) : ChildCView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ChildCView> = ViewFactory {
            ChildCViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
