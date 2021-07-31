package com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.simple_routing.rib.R

interface Child1Child1View : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, Child1Child1View>
}

class Child1Child1ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), Child1Child1View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_child1_child1
    ) : Child1Child1View.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<Child1Child1View> =
            ViewFactory {
                Child1Child1ViewImpl(
                    androidView = it.inflate(layoutRes)
                )
            }
    }

}
