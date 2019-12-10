package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.Wish.Execute
import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration
import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration.Content
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.view.RibView
import io.reactivex.ObservableSource

internal class RecyclerViewHostInteractor<T : Parcelable>(
    savedInstanceState: Bundle? = null,
    router: RecyclerViewHostRouter<*>,
    private val input: ObservableSource<Input<T>>,
    private val feature: RecyclerViewHostFeature<T>,
    private val adapter: Adapter<T>
) : Interactor<Configuration, Content, Nothing, RibView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle) {
        ribLifecycle.createDestroy {
            bind(feature to adapter) // TODO consider viewLifecycle
            bind(input to feature using { Execute(it) })
        }
    }
}
