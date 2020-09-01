package com.badoo.ribs.android.recyclerview

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView

internal class RecyclerViewHostInteractor<T : Parcelable>(
    buildParams: BuildParams<*>,
    private val feature: RecyclerViewHostFeature<T>,
    private val adapter: Adapter<T>
) : Interactor<RecyclerViewHost<T>, RibView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(feature to adapter) // TODO consider viewLifecycle
            bind(rib.input to feature)
        }
    }
}
