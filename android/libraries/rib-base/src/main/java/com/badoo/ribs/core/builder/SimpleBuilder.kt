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
package com.badoo.ribs.core.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib

/**
 * Responsible for building a [Node]. Parent [Router]s should pass in static dependencies via the
 * dependency passed in via the constructor. For dynamic dependencies (things that are fetched
 * asynchronously - or created dynamically in the parent), they should be passed in via a build
 * method that vends a node.
 *
 * @param <D> type of dependency required to build the interactor.
 * @param <N> type of [Node] this Builder is expected to build
 *
</D> */
abstract class SimpleBuilder<N : Node<*>>(
    rib: Rib
) : Builder<Nothing?, N>() {

    fun build(buildContext: BuildContext): N =
        build(buildContext, null)
}


