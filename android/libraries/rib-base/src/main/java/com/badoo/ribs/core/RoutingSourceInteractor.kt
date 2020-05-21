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
import com.badoo.ribs.core.routing.RoutingSource
import com.badoo.ribs.core.routing.RoutingSourceFactory
import com.badoo.ribs.core.view.RibView
import io.reactivex.disposables.Disposable

/**
 * The base implementation for all [RoutingSourceInteractor]s.
 *
 * @param <C> the type of Configuration this Interactor can expect to push to its [Router].
 * @param <V> the type of [RibView].
 **/
abstract class RoutingSourceInteractor<R : Rib, V : RibView, C : Parcelable, RS : RoutingSource<C>>(
    buildParams: BuildParams<*>,
    factory: RoutingSourceFactory<C, RS>,
    protected val routing: RS = factory.invoke(buildParams),
    disposables: Disposable? = null
) : Interactor<R, V>(
    buildParams = buildParams,
    disposables = disposables
), RoutingSource<C> by routing {

}
