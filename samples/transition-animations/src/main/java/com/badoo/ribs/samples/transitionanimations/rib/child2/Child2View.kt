package com.badoo.ribs.samples.transitionanimations.rib.child2

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.transitionanimations.R

interface Child2View : RibView {

    interface Factory : ViewFactory<Nothing?, Child2View>
}

class Child2ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    Child2View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child2
    ) : Child2View.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> Child2View = {
            Child2ViewImpl(
                it.inflate(layoutRes)
            )
        }
    }
}
