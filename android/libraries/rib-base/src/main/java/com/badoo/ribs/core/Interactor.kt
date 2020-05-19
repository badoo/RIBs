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

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.view.RibView
import io.reactivex.disposables.Disposable

/**
 * The base implementation for all [Interactor]s.
 *
 * @param <V> the type of [RibView].
 **/
abstract class Interactor<R : Rib, V : RibView>(
    buildParams: BuildParams<*>,
    private val disposables: Disposable?,
    private val ribAware: RibAware<R> = RibAwareImpl()
) : Identifiable by buildParams.identifier,
    RibAware<R> by ribAware,
    BackPressHandler,
    NodeLifecycleAware,
    SubtreeChangeAware,
    SavesInstanceState,
    ViewAware<V> {

    override fun onDetach() {
        disposables?.dispose()
    }

    /**
     * Handle an activity back press.
     *
     * @return TRUE if the interactor handled the back press and no further action is necessary.
     */
    open fun handleBackPress(): Boolean =
        false
}
