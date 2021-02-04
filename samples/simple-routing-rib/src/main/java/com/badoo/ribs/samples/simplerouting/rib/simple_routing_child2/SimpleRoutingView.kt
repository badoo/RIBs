package com.badoo.ribs.samples.simplerouting.rib.simple_routing_child2

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.simplerouting.rib.R

interface SimpleRoutingChild2View : RibView {

    interface Factory : ViewFactory<Nothing?, SimpleRoutingChild2View>

    fun showInitialMessage()
}

class SimpleRoutingChild2ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), SimpleRoutingChild2View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_child2
    ) : SimpleRoutingChild2View.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> SimpleRoutingChild2View = {
            SimpleRoutingChild2ViewImpl(
                    androidView = it.inflate(layoutRes)
            )
        }
    }

    private val titleTextView: TextView = androidView.findViewById(R.id.content_text)

    override fun showInitialMessage() {
        titleTextView.text = context.getString(R.string.child2_text)
    }
}
