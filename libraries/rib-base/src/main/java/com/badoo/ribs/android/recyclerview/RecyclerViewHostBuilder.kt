package com.badoo.ribs.android.recyclerview

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.routing.router.RouterByDelegate
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.routing.source.impl.Pool

@ExperimentalApi
class RecyclerViewHostBuilder<T : Parcelable>(
    private val dependency: RecyclerViewHost.Dependency<T>
) : SimpleBuilder<RecyclerViewHost<T>>() {

    @SuppressWarnings("LongMethod")
    override fun build(buildParams: BuildParams<Nothing?>): RecyclerViewHost<T> {
        val timeCapsule = AndroidTimeCapsule(buildParams.savedInstanceState)

        val routingSource = Pool<T>(
            allowRepeatingConfigurations = true
        )

        val feature = RecyclerViewHostFeature(
            timeCapsule = timeCapsule,
            initialElements = dependency.initialElements()
        )

        val adapter = Adapter(
            hostingStrategy = dependency.hostingStrategy(),
            initialEntries = feature.state.items,
            routingSource = routingSource,
            feature = feature,
            viewHolderLayoutParams = dependency.viewHolderLayoutParams()
        )

        val router = RouterByDelegate(
            buildParams = buildParams,
            routingSource = routingSource,
            resolver = dependency.resolver(),
            clientChildActivator = adapter
        )

        val interactor = RecyclerViewHostInteractor(
            buildParams = buildParams,
            feature = feature,
            adapter = adapter
        )

        val viewDeps = object : RecyclerViewHostView.Dependency {
            override fun adapter(): Adapter<*> = adapter
            override fun recyclerViewFactory(): RecyclerViewFactory = dependency.recyclerViewFactory()
            override fun layoutManagerFactory(): LayoutManagerFactory = dependency.layoutManagerFactory()
        }

        return RecyclerViewHostNode(
            buildParams = buildParams,
            plugins = listOf(
                router,
                interactor
            ),
            viewDeps =  viewDeps,
            timeCapsule = timeCapsule,
            adapter = adapter
        )
    }
}
