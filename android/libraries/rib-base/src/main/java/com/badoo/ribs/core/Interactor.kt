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
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.view.RibView
import io.reactivex.disposables.Disposable

/**
 * The base implementation for all [Interactor]s.
 *
 * @param <V> the type of [RibView].
 **/
abstract class Interactor<V : RibView>(
    buildParams: BuildParams<*>,
    private val disposables: Disposable?
) : Identifiable by buildParams.identifier,
    NodeAware,
    BackPressHandler,
    NodeLifecycleAware,
    SavesInstanceState,
    ViewAware<V> {

    protected lateinit var node: Node<*>
        private set

    override fun init(node: Node<*>) {
        this.node = node
    }

    override fun onDetach() {
        disposables?.dispose()
    }
}
