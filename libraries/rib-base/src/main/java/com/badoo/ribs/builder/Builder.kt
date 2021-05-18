package com.badoo.ribs.builder

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.RibAware

/**
 * Responsible for building a [Rib]. Dependencies available compile-time should be provided via the
 * the constructor. Dynamic dependencies (things that are available only run-time) should be passed
 * in via a <P> payload.
 *
 * @param <P> type of payload that's only known build-time (e.g. user id to build a profile module for)
 * @param <T> type of [Rib] this Builder is expected to build
 *
 */
abstract class Builder<P, T : Rib> {

    fun build(buildContext: BuildContext, payload: P): T {
        val buildParams = BuildParams(
            payload = payload,
            buildContext = buildContext
        )
        return build(buildParams).also { rib ->
            rib.node.plugins<RibAware<T>>().forEach { plugin ->
                plugin.init(rib)
            }

            rib.node.onBuildFinished()
        }
    }

    protected abstract fun build(buildParams: BuildParams<P>): T
}


