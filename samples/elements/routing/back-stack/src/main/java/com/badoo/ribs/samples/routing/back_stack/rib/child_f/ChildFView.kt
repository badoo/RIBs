package com.badoo.ribs.samples.routing.back_stack.rib.child_f

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.back_stack.R

interface ChildFView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, ChildFView>

}


class ChildFViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildFView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_f
    ) : ChildFView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ChildFView> = ViewFactory {
            ChildFViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
