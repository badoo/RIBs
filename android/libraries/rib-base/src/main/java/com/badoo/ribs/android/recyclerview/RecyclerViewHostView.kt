package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.recyclerview.RecyclerViewHostView.Dependency
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory


internal interface RecyclerViewHostView : RibView {

    interface Factory : ViewFactory<Dependency, RecyclerViewHostView>

    interface Dependency {
        fun router(): RecyclerViewHostRouter<*>
        fun adapter(): Adapter<*>
        fun feature(): RecyclerViewHostFeature<*>
    }
}

internal class RecyclerViewHostViewImpl private constructor(
    override val androidView: RecyclerView,
//    private val router: RecyclerViewHostRouter<*>,
    private val feature: RecyclerViewHostFeature<*>
//    private val ribPager: RibPager<*>
) : RecyclerViewHostView {

    class Factory: RecyclerViewHostView.Factory {
        override fun invoke(deps: Dependency): (ViewGroup) -> RecyclerViewHostView = {
            val router = deps.router()

            RecyclerViewHostViewImpl(
                androidView = RecyclerView(it.context).apply {
                    adapter = deps.adapter()
                    layoutManager = LinearLayoutManager(it.context) // TODO from deps / customisation
                    addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
                        override fun onChildViewAttachedToWindow(view: View) {
                            Log.d("RecyclerViewHostView", "onChildViewAttachedToWindow: ${view.tag} / $view")
                            val configurationKey = view.tag as ConfigurationKey
                            router.activate(configurationKey)
                            router.getNodes(configurationKey)!!.forEach { childNode ->
                                childNode.attachToView(view as ViewGroup)
                            }
                        }

                        override fun onChildViewDetachedFromWindow(view: View) {
                            Log.d("RecyclerViewHostView", "onChildViewDetachedFromWindow: ${view.tag} / $view")
                            val configurationKey = view.tag as ConfigurationKey
                            router.deactivate(configurationKey)
                            router.getNodes(configurationKey)!!.forEach { childNode ->
                                childNode.detachFromView()
                            }
                        }
                    })
//                    setRecyclerListener {
//                        it.itemView
//                    }
                },
//                router = deps.router(),
                feature = deps.feature()
            )
        }
    }

//    fun onSaveInstanceState(state: Bundle) {
//        super.onSaveInstanceState(state)
//        // Save list state
//        mListState = mLayoutManager.onSaveInstanceState()
//        state.putParcelable(LIST_STATE_KEY, mListState)
//    }

    override fun getParentViewForChild(child: Node<*>): ViewGroup? {
        val cc = (child.ancestryInfo as AncestryInfo.Child).creatorConfiguration
        val ccc = cc as RecyclerViewHostRouter.Configuration.Content.Item
        val position = feature.state.items.indexOfFirst { it.uuid == ccc.uuid }
        val holder = androidView.findViewHolderForAdapterPosition(position)
        return holder!!.itemView as FrameLayout
    }
}
