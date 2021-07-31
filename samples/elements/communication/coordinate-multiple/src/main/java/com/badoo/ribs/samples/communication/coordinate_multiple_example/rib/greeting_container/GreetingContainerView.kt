package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container

import android.view.ViewGroup
import android.widget.FrameLayout
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder

interface GreetingContainerView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, GreetingContainerView>
}

class GreetingContainerViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    GreetingContainerView {

    class Factory : GreetingContainerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<GreetingContainerView> = ViewFactory { context ->
            GreetingContainerViewImpl(
                FrameLayout(context.parent.context)
            )
        }
    }
}
