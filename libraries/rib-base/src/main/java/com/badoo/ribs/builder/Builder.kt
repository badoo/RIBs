package com.badoo.ribs.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.RibAware
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
 */
abstract class Builder<P, T : Rib> {

    fun build(buildContext: BuildContext, payload: P): T {
        val buildParams = BuildParams(
            payload = payload,
            buildContext = buildContext,
            identifier = Rib.Identifier(
                uuid = buildContext.savedInstanceState?.getSerializable(Rib.Identifier.KEY_UUID) as? UUID
                    ?: UUID.randomUUID()
            )
        )
        return build(buildParams).also { rib ->
            rib.node.plugins<RibAware<T>>().forEach { plugin ->
                plugin.init(rib)
            }

            rib.node.onCreate()
        }
    }

    protected abstract fun build(buildParams: BuildParams<P>): T
}


