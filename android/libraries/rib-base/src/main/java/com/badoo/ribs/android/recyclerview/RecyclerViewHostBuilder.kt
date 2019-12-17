package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.Builder

class RecyclerViewHostBuilder<T : Parcelable>(
    override val dependency: RecyclerViewHost.Dependency<T>
) : Builder<RecyclerViewHost.Dependency<T>>() {

    @SuppressWarnings("LongMethod")
    fun build(savedInstanceState: Bundle? = null): RecyclerViewHostNode<T> {
        val timeCapsule = AndroidTimeCapsule(savedInstanceState)

        val feature = RecyclerViewHostFeature(
            timeCapsule = timeCapsule,
            initialElements = dependency.initialElements()
        )

        val router = RecyclerViewHostRouter(
            savedInstanceState = savedInstanceState,
            feature = feature,
            ribResolver = dependency.resolver()
        )

        val adapter = Adapter(
            hostingStrategy = dependency.hostingStrategy(),
            initialEntries = feature.state.items,
            router = router,
            viewHolderLayoutParams = dependency.viewHolderLayoutParams()
        )

        val interactor = RecyclerViewHostInteractor(
            savedInstanceState = savedInstanceState,
            input = dependency.recyclerViewHostInput(),
            feature = feature,
            adapter = adapter
        )

        return RecyclerViewHostNode(
            savedInstanceState = savedInstanceState,
            router = router,
            interactor = interactor,
            viewDeps =  object : RecyclerViewHostView.Dependency {
                override fun adapter(): Adapter<*> = adapter
                override fun layoutManagerFactory(): LayoutManagerFactory = dependency.layoutManagerFactory()
            },
            timeCapsule = timeCapsule,
            adapter = adapter
        )
    }
}
