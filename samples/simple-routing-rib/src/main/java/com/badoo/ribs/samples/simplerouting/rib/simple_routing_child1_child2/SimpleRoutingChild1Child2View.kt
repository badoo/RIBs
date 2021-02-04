package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child2

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.simplerouting.rib.R

interface SimpleRoutingChild1Child2View : RibView {

    interface Factory : ViewFactory<Nothing?, SimpleRoutingChild1Child2View>
}

class SimpleRoutingChild1Child2ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), SimpleRoutingChild1Child2View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_child1_child2
    ) : SimpleRoutingChild1Child2View.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> SimpleRoutingChild1Child2View = {
            SimpleRoutingChild1Child2ViewImpl(
                    androidView = it.inflate(layoutRes)
            )
        }
    }
}
