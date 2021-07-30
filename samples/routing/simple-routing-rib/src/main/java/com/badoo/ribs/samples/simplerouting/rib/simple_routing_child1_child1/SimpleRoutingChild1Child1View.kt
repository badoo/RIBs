package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child1_child1

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.simplerouting.rib.R

interface SimpleRoutingChild1Child1View : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, SimpleRoutingChild1Child1View>
}

class SimpleRoutingChild1Child1ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), SimpleRoutingChild1Child1View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_child1_child1
    ) : SimpleRoutingChild1Child1View.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<SimpleRoutingChild1Child1View> =
            ViewFactory {
                SimpleRoutingChild1Child1ViewImpl(
                    androidView = it.inflate(layoutRes)
                )
            }
    }

}
