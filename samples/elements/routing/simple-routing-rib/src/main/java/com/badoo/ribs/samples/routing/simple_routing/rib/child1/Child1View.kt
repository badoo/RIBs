package com.badoo.ribs.samples.routing.simple_routing.rib.child1

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.simple_routing.rib.R
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child1.Child1Child1
import com.badoo.ribs.samples.routing.simple_routing.rib.child1_child2.Child1Child2

interface Child1View : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, Child1View>

    fun setTitle(title: String)
}

class Child1ViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(), Child1View {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_simple_routing_child1
    ) : Child1View.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<Child1View> = ViewFactory {
            Child1ViewImpl(
                androidView = it.inflate(layoutRes)
            )
        }
    }

    private val child1Child2Container: ViewGroup = androidView.findViewById(R.id.child1_child1_container)
    private val child2Child2Container: ViewGroup = androidView.findViewById(R.id.child1_child2_container)
    private val textView: TextView = androidView.findViewById(R.id.child1_title)

    override fun setTitle(title: String) {
        textView.text = title
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is Child1Child1 -> child1Child2Container
            is Child1Child2 -> child2Child2Container
            else -> super.getParentViewForSubtree(subtreeOf)
        }
}
