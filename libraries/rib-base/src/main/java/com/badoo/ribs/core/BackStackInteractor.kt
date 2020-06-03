/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.badoo.ribs.core

import android.os.Parcelable
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.core.view.RibView
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
