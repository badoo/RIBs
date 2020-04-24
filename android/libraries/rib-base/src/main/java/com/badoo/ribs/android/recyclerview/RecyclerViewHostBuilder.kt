package com.badoo.ribs.android.recyclerview

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class RecyclerViewHostBuilder<T : Parcelable>(
    private val dependency: RecyclerViewHost.Dependency<T>
) : SimpleBuilder<RecyclerViewHostNode<T>>() {

    @SuppressWarnings("LongMethod")
    override fun build(buildParams: BuildParams<Nothing?>): RecyclerViewHostNode<T> {
        val timeCapsule = AndroidTimeCapsule(buildParams.savedInstanceState)

        val feature = RecyclerViewHostFeature(
            timeCapsule = timeCapsule,
            initialElements = dependency.initialElements()
        )

        val router = RecyclerViewHostRouter(
            buildParams = buildParams,
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
            buildParams = buildParams,
            input = dependency.recyclerViewHostInput(),
            feature = feature,
            adapter = adapter
        )

        return RecyclerViewHostNode(
            buildParams = buildParams,
            router = router,
            interactor = interactor,
            viewDeps =  object : RecyclerViewHostView.Dependency {
                override fun adapter(): Adapter<*> = adapter
                override fun recyclerViewFactory(): RecyclerViewFactory = dependency.recyclerViewFactory()
                override fun layoutManagerFactory(): LayoutManagerFactory = dependency.layoutManagerFactory()
            },
            timeCapsule = timeCapsule,
            adapter = adapter
        )
    }
}
