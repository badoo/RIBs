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

import androidx.lifecycle.Lifecycle
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CallSuper
import com.badoo.ribs.core.view.RibView
import io.reactivex.disposables.Disposable
import java.util.UUID

/**
 * The base implementation for all [Interactor]s.
 *
 * @param <C> the type of Configuration this Interactor can expect to push to its [Router].
 * @param <V> the type of [RibView].
 **/
abstract class Interactor<C : Parcelable, Content : C, Overlay : C, V : RibView>(
    savedInstanceState: Bundle?,
    protected val router: Router<C, *, Content, Overlay, V>,
    private val disposables: Disposable?
) : Identifiable {

    private val savedInstanceState = savedInstanceState?.getBundle(BUNDLE_KEY)

    internal var tag = this.savedInstanceState?.getString(KEY_TAG) ?: "${this::class.java.name}.${UUID.randomUUID()}"
        private set

    override val id: String
        get() = tag

    internal open fun onAttach(ribLifecycle: Lifecycle) {
        onAttach(ribLifecycle, savedInstanceState)
    }

    protected open fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        // TODO remove this method
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

    @CallSuper
    open fun onSaveInstanceState(outState: Bundle) {
        val bundle = Bundle()
        bundle.putString(KEY_TAG, tag)
        outState.putBundle(BUNDLE_KEY, bundle)
    }

    companion object {
        internal const val BUNDLE_KEY = "Interactor"
        internal const val KEY_TAG = "interactor.tag"
    }
}
