package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.view.RibView
import io.reactivex.ObservableSource

internal class RecyclerViewHostInteractor<T : Parcelable>(
    savedInstanceState: Bundle? = null,
    private val input: ObservableSource<Input<T>>,
    private val feature: RecyclerViewHostFeature<T>,
    private val adapter: Adapter<T>
) : Interactor<RibView>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle) {
        ribLifecycle.createDestroy {
            bind(feature to adapter) // TODO consider viewLifecycle
            bind(input to feature)
        }
    }
}
