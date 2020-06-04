package com.badoo.ribs.clienthelper.interactor

import android.os.Parcelable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import io.reactivex.disposables.Disposable

/**
 * Helper class for easier migration.
 *
 * Suggested to rather extend Interactor, create and inject back stack / other RoutingSource manually.
 */
abstract class BackStackInteractor<R : Rib, V : RibView, C : Parcelable>(
    buildParams: BuildParams<*>,
    disposables: Disposable? = null,
    val backStack: BackStackFeature<C>
) : Interactor<R, V>(
    buildParams = buildParams,
    disposables = disposables
), RoutingSource<C> by backStack {

    constructor(
        buildParams: BuildParams<*>,
        initialConfiguration: C,
        disposables: Disposable? = null
    ) : this(
        buildParams = buildParams,
        disposables = disposables,
        backStack = BackStackFeature(
            initialConfiguration = initialConfiguration,
            buildParams = buildParams
        )
    )
}
