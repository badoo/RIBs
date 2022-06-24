package com.badoo.ribs.samples.routing.transition_animations.rib.child3

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.transition_animations.R

interface Child3View : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, Child3View>
}

class Child3ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    Child3View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_child3
    ) : Child3View.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<Child3View> =
            ViewFactory {
                Child3ViewImpl(
                    it.inflate(layoutRes)
                )
            }
    }
}
