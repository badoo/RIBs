package com.badoo.ribs.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

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
abstract class SimpleBuilder<T : Rib> : Builder<Nothing?, T>() {

    fun build(buildContext: BuildContext): T =
        build(buildContext, null)
}


