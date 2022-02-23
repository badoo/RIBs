package com.badoo.ribs.samples.routing.simple_routing.rib.child2

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.simple_routing.rib.R

interface Child2View : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, Child2View>

    fun showInitialMessage()
}

class Child2ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), Child2View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_child2
    ) : Child2View.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<Child2View> = ViewFactory {
            Child2ViewImpl(
                androidView = it.inflate(layoutRes)
            )
        }
    }

    private val titleTextView: TextView = androidView.findViewById(R.id.content_text)

    override fun showInitialMessage() {
        titleTextView.text = context.getString(R.string.child2_text)
    }
}
