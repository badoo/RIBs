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

import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import java.util.UUID

/**
 * Responsible for building a [Node]. Parent [Router]s should pass in static dependencies via the
 * dependency passed in via the constructor. For dynamic dependencies (things that are fetched
 * asynchronously - or created dynamically in the parent), they should be passed in via a build
 * method that vends a node.
 *
 * @param <D> type of dependency required to build the interactor.
 * @param <P> type of parameters that are only known build-time and not ahead (e.g. user id of another user to build the RIB for)
 * @param <N> type of [Node] this Builder is expected to build
 *
</D> */
abstract class Builder<D, P, N : Node<*>> {
    abstract val dependency: D

    fun build(params: BuildContext.Params): N =
        build(params.with())

    abstract fun build(params: BuildContext.ParamsWithData<P>): N

    /**
     * Helper method to create [BuildContext.Resolved] that is then passed to RIB components.
     */
    // TODO test
    protected fun resolve(
        rib: Rib,
        buildContext: BuildContext.ParamsWithData<P>
    ): BuildContext.Resolved<P> =
            BuildContext.Resolved(
                ancestryInfo = buildContext.ancestryInfo,
                viewAttachMode = buildContext.viewAttachMode,
                savedInstanceState = buildContext.savedInstanceState,
                identifier = Rib.Identifier(
                    rib = rib,
                    uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID ?: UUID.randomUUID(),
                    tag = buildContext.tag
                ),
                data = buildContext.data
            )
}


