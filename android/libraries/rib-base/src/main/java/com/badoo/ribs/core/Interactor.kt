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
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.RibLifecycleAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.view.RibView

/**
 * The base implementation for all [Interactor]s.
 *
 * @param <C> the type of Configuration this Interactor can expect to push to its [Router].
 * @param <V> the type of [RibView].
 **/
abstract class Interactor<V : RibView>(
    buildParams: BuildParams<*>,
    private val disposables: Disposable?
) : Identifiable by buildParams.identifier,
    NodeAware,
    BackPressHandler,
    RibLifecycleAware,
    ViewAware<V> {

    protected lateinit var node: Node<*>
        private set

    override fun init(node: Node<*>) {
        this.node = node
    }

    internal fun onDetach() {
        disposables?.dispose()
    }

    internal fun onViewCreated(viewLifecycle: Lifecycle, view: V) {
        onViewCreated(view, viewLifecycle)
    }

    protected open fun onViewCreated(view: V, viewLifecycle: Lifecycle) {
    }

    /**
     * Handle an activity back press.
     *
     * @return TRUE if the interactor handled the back press and no further action is necessary.
     */
    open fun handleBackPress(): Boolean =
        false

    @CallSuper // FIXME cleanup / remove
    open fun onSaveInstanceState(outState: Bundle) {
        val bundle = Bundle()
        outState.putBundle(BUNDLE_KEY, bundle)
    }

    companion object {
        internal const val BUNDLE_KEY = "Interactor"
        internal const val KEY_TAG = "interactor.tag"
    }
}
