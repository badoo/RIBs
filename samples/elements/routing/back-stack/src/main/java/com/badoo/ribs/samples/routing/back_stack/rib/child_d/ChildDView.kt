package com.badoo.ribs.samples.routing.back_stack.rib.child_d

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.back_stack.R

interface ChildDView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, ChildDView>

}


class ChildDViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ChildDView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child_d
    ) : ChildDView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ChildDView> = ViewFactory {
            ChildDViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

}
