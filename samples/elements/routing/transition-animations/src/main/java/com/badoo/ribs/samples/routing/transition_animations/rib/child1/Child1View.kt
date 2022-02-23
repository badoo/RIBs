package com.badoo.ribs.samples.routing.transition_animations.rib.child1

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.transition_animations.R

interface Child1View : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, Child1View>
}


class Child1ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    Child1View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child1
    ) : Child1View.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<Child1View> =
            ViewFactory {
                Child1ViewImpl(
                    it.inflate(layoutRes)
                )
            }
    }
}
