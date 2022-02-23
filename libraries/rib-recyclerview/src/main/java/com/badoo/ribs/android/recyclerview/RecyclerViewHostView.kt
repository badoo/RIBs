package com.badoo.ribs.android.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.recyclerview.RecyclerViewHostView.Dependency
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder


internal interface RecyclerViewHostView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, RecyclerViewHostView>

    interface Dependency {
        fun adapter(): Adapter<*>
        fun recyclerViewFactory(): RecyclerViewFactory
        fun layoutManagerFactory(): LayoutManagerFactory
    }
}

internal class RecyclerViewHostViewImpl private constructor(
    override val androidView: RecyclerView
) : AndroidRibView(),
    RecyclerViewHostView {

    class Factory : RecyclerViewHostView.Factory {
        override fun invoke(deps: Dependency): ViewFactory<RecyclerViewHostView> = ViewFactory {
            RecyclerViewHostViewImpl(
                androidView = deps
                    .recyclerViewFactory()
                    .invoke(it.context)
                    .apply {
                        adapter = deps.adapter()
                        layoutManager =
                            deps
                                .layoutManagerFactory()
                                .invoke(it.context)
                    }
            )
        }
    }
}
